9/6/2021
Demo jackson json:
- Read json text file from resources/ folder
- Un/marshall json to/from java POJO
- Print pretty
- json nested structure can be mapped to POJO

Notes:
- Field name must match json or use @JsonProperties
- Fields in POJO but not in json will have null values (different from @JsonIgnore)
