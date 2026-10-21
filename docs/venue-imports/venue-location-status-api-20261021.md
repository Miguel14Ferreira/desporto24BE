# Jynlo — read-only venue location status

The HTTP response now has an additive locationStatus JSON property, derived without database/schema/API-write changes.

* GPS_ONLY_ADDRESS_UNKNOWN: usable latitude/longitude but the address is absent or a clearly marked OSM map placeholder.
* ADDRESS_PROVIDED_UNVERIFIED: a human-readable address is present, but there is NO evidence field proving postal verification.
* LOCATION_UNKNOWN: missing address/placeholder AND unusable GPS.

This is NOT a statement of opening, access rights, booking, postal confirmation, or approval. Existing 265 LIVE venues remain unchanged; community creation and admin import still require an address. This does not import any GPS-only records or change the Liquibase master.

The frontend should consume this property with a backwards-compatible fallback, and should hide raw GPS placeholders under the 'Address' label. A separate opt-in schema and DTO change is necessary before true nullable addresses can be stored.
