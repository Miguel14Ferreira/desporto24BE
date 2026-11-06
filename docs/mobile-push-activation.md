# Jynlo Mobile push — staged activation

The backend now has an optional, bounded Expo Push Service transport. **It is disabled by default.**

## Activation prerequisites

1. Link the mobile application to its real EAS project ID (do not invent an ID).
2. Configure Android FCM v1 and, for iOS builds, APNs credentials in the owner-controlled Expo/EAS project.
3. Verify device registration and revocation on an installed release build.
4. Implement and test a durable Expo push ticket/receipt collector and its DeviceNotRegistered cleanup before enabling production sending. Expo recommends checking receipts about 15 minutes after submission. A push *ticket* is not proof of delivery.
5. Verify account preferences, notification mutes, rate limiting and logout in a staging environment.

## Server configuration

- `JYNLO_PUSH_ENABLED`: `false` by default. **Keep false until receipt monitoring is complete.**
- `JYNLO_PUSH_ACCESS_TOKEN`: Expo access token for the configured project. Supply only through the deployment environment's secret store. Never commit it.

The transport uses `https://exp.host/--/api/v2/push/send` via a bounded asynchronous executor, with short timeouts. The payload contains only a generic message and notification ID; it never transmits private message contents, group membership, usernames, email addresses or file URLs.

Events are emitted when `NotificationServiceImpl.save` persists allowed notifications, and are handled after DB commit. No pushes are emitted from rolled-back transactions. Preferences are rechecked at the time of delivery.

The native app presents an explicit opt-in flow and routes notification taps to the authenticated notification inbox. With no configured EAS project ID, it displays a configuration error instead of registering a fake token.

## Known limitations

- Expo receipt persistence, delayed receipt polling, durable retries and observability are **not yet implemented**. Do not claim production push functionality.
- There is no device-level cross-account reassignment. A registered device ID belongs to one account until revoked; logout attempts revocation before bearer invalidation.
- Android/iOS physical-device validation and CI build results are separate requirements.
- Existing notification categories and mute preferences must be audited before broad delivery is enabled.
