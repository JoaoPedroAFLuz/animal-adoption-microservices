Analyze the given code for quality issues following the project's existing patterns. Check for:

- Bugs and logic errors
- Missing or incorrect validations (@NotNull vs @NotBlank, enum handling)
- Race conditions or concurrency issues
- Unused imports or dead code
- Missing error handling
- JPA entity exposure in API responses (should use DTOs)
- Cache consistency (missing @CacheEvict)
- Security gaps (missing @PreAuthorize, exposed endpoints)
- Test coverage gaps
