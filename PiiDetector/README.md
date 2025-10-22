## Supported PII Types

| PII Type | Detection Method | Validation | Test Status | Notes |
|----------|-----------------|------------|-------------|-------|
| **Email** | Regex | Format validation | ✅ Working | Detects standard email formats |
| **SSN** | Regex | Area/Group/Serial validation | ✅ Working | Rejects invalid area codes (000, 666, 900+), group 00, serial 0000 |
| **Phone Number** | Regex | Format validation | ✅ Working | Supports multiple US formats: (555) 123-4567, 555-123-4567, etc. |
| **Credit Card** | Regex | Luhn algorithm | ✅ Working | Supports 13-19 digits (Visa, Mastercard, Amex, Discover) |
| **Person Names** | Stanford NER | - | ✅ Working | Best with full names; single first names may not be detected |
| **Locations** | Stanford NER | - | ✅ Working | Detects cities, states, countries, addresses |
| **Organizations** | Stanford NER | - | ✅ Working | Detects company names, institutions |
| **Dates** | Stanford NER | - | ✅ Working | Various date formats; excludes SSN-like patterns |
| **Money** | Stanford NER | - | ✅ Working | Detects currency amounts ($100, €50, etc.) |
| **IP Address (IPv4)** | Regex | Format validation | ✅ Working | IPv4 only; IPv6 not supported |
| **ZIP Code** | Regex | Format validation | ✅ Working | 5-digit and ZIP+4 formats |
| **URL** | Regex | Format validation | ✅ Working | HTTP/HTTPS URLs |