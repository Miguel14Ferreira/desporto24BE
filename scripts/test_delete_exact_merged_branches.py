"""Offline regression tests for allowlisted branch deletion."""
import importlib.util
import io
import os
import pathlib
import unittest
from contextlib import redirect_stdout
from unittest.mock import patch

SOURCE = pathlib.Path(__file__).with_name("delete_exact_merged_branches.py")
spec = importlib.util.spec_from_file_location("allowlisted_cleanup", SOURCE)
mod = importlib.util.module_from_spec(spec)
spec.loader.exec_module(mod)

class AllowlistTests(unittest.TestCase):
    def run_case(self, status="behind", ahead=0, confirm="", pr=None, changed=False):
        calls = []
        branch = mod.ALLOWLIST[0]
        old = "a" * 40
        new = "b" * 40
        def fake(method, path, token, body=None):
            calls.append((method, path))
            if path == "git/ref/heads/develop":
                return 200, {"object": {"sha": new}}
            if path.startswith("compare/"):
                return 200, {"status": status, "ahead_by": ahead}
            if path.startswith("pulls?"):
                return 200, pr if pr is not None else []
            if method == "DELETE":
                return 204, None
            if path == "git/ref/heads/" + branch:
                refs_seen = sum(1 for m, p in calls if m == "GET" and p == path)
                return 200, {"object": {"sha": new if changed and refs_seen > 1 else old}}
            raise AssertionError((method, path))
        with patch.object(mod, "ALLOWLIST", [branch]), patch.object(mod, "github", side_effect=fake), patch.dict(os.environ, {"GH_TOKEN":"fake", "CONFIRM_DELETE":confirm}), redirect_stdout(io.StringIO()) as output:
            try:
                mod.main()
                error = None
            except RuntimeError as exc:
                error = str(exc)
        return calls, output.getvalue(), error

    def test_dry_run_is_default(self):
        calls, out, error = self.run_case()
        self.assertIsNone(error)
        self.assertIn("DRY_RUN", out)
        self.assertFalse(any(method == "DELETE" for method, _ in calls))

    def test_deletes_only_explicitly_confirmed_ancestor(self):
        calls, out, error = self.run_case(confirm="DELETE_EXACT_ALLOWLIST")
        self.assertIsNone(error)
        self.assertEqual(sum(method == "DELETE" for method, _ in calls), 1)

    def test_divergent_commit_rejected(self):
        calls, _, error = self.run_case(status="diverged", ahead=1, confirm="DELETE_EXACT_ALLOWLIST")
        self.assertIsNotNone(error)
        self.assertFalse(any(method == "DELETE" for method, _ in calls))

    def test_open_pull_request_rejected(self):
        calls, _, error = self.run_case(pr=[{"number":1}], confirm="DELETE_EXACT_ALLOWLIST")
        self.assertIsNotNone(error)
        self.assertFalse(any(method == "DELETE" for method, _ in calls))

    def test_changed_sha_rejected(self):
        calls, _, error = self.run_case(changed=True, confirm="DELETE_EXACT_ALLOWLIST")
        self.assertIsNotNone(error)
        self.assertFalse(any(method == "DELETE" for method, _ in calls))

if __name__ == "__main__":
    unittest.main()
