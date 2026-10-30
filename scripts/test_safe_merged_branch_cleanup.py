#!/usr/bin/env python3
"""Regression tests for safe_merged_branch_cleanup (no GitHub mutations)."""
import contextlib
import datetime
import io
import json
import os
import runpy
import subprocess
import unittest
from unittest.mock import patch
from urllib.error import HTTPError

SCRIPT = os.path.join(os.path.dirname(__file__), "safe_merged_branch_cleanup.py")
NOW = datetime.datetime.now(datetime.timezone.utc)
OLD = (NOW - datetime.timedelta(days=30)).isoformat()


def completed(args, code=0, stdout=""):
    return subprocess.CompletedProcess(args, code, stdout, "")


class FakeResponse:
    def __init__(self, status, value):
        self.status = status
        self.value = value

    def __enter__(self):
        return self

    def __exit__(self, *_):
        return False

    def read(self):
        return json.dumps(self.value).encode() if self.value is not None else b""


class CleanupTests(unittest.TestCase):
    def execute(self, dry_run="true", remote_sha="abc123", raise_api=False, tree_equivalent=False):
        requests = []

        def fake_git(args, **kwargs):
            cmd = list(args)
            if "for-each-ref" in cmd:
                return completed(cmd, stdout="feature/old-merged\t" + OLD + "\n")
            if "merge-base" in cmd:
                return completed(cmd, code=1 if tree_equivalent else 0)
            if "diff" in cmd:
                return completed(cmd, code=0 if tree_equivalent else 1)
            if "rev-parse" in cmd:
                return completed(cmd, stdout="abc123\n")
            raise AssertionError("Unexpected git command " + str(cmd))

        def fake_urlopen(req, timeout=20):
            requests.append((req.get_method(), req.full_url))
            if raise_api:
                raise HTTPError(req.full_url, 403, "Forbidden", {}, None)
            if "/pulls?" in req.full_url:
                return FakeResponse(200, [])
            if "/git/ref/heads/" in req.full_url:
                return FakeResponse(200, {"object": {"sha": remote_sha}})
            if req.get_method() == "DELETE":
                return FakeResponse(204, None)
            raise AssertionError("Unexpected API request " + req.full_url)

        env = {
            "GITHUB_REPOSITORY": "example/repo",
            "GITHUB_TOKEN": "testing-only",
            "PRODUCTION_BRANCH": "develop",
            "MIN_AGE_DAYS": "3",
            "DRY_RUN": dry_run,
            "GITHUB_REF_NAME": "tests",
        }
        output = io.StringIO()
        with patch.dict(os.environ, env), patch("subprocess.run", side_effect=fake_git), patch(
            "urllib.request.urlopen", side_effect=fake_urlopen
        ), contextlib.redirect_stdout(output):
            if raise_api:
                with self.assertRaises(RuntimeError):
                    runpy.run_path(SCRIPT, run_name="__main__")
            elif remote_sha != "abc123" and dry_run == "false":
                with self.assertRaisesRegex(RuntimeError, "Candidate changed remotely"):
                    runpy.run_path(SCRIPT, run_name="__main__")
            else:
                runpy.run_path(SCRIPT, run_name="__main__")
        return requests, output.getvalue()

    def test_dry_run_never_deletes(self):
        requests, output = self.execute()
        self.assertFalse(any(method == "DELETE" for method, _ in requests))
        self.assertEqual(json.loads(output)["candidates"], 1)

    def test_divergent_history_with_equal_tree_is_reported_not_deleted(self):
        requests, output = self.execute(dry_run="false", tree_equivalent=True)
        self.assertFalse(any(method == "DELETE" for method, _ in requests))
        self.assertEqual(json.loads(output)["content_equivalent_count"], 1)
        self.assertEqual(json.loads(output)["candidates"], 0)

    def test_api_error_fails_closed(self):
        requests, _ = self.execute(raise_api=True)
        self.assertFalse(any(method == "DELETE" for method, _ in requests))

    def test_changed_sha_never_deletes(self):
        requests, _ = self.execute(dry_run="false", remote_sha="different")
        self.assertFalse(any(method == "DELETE" for method, _ in requests))

    def test_confirmed_sha_and_204_counts_delete(self):
        requests, output = self.execute(dry_run="false")
        self.assertEqual(sum(method == "DELETE" for method, _ in requests), 1)
        self.assertEqual(json.loads(output)["deleted_attempts"], 1)


if __name__ == "__main__":
    unittest.main()
