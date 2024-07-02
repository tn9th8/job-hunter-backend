## Về tác giả
Nguyễn Trung Nhân

## Rest API
Các khái niệm:
- Rest API,
- JSON,
- API,
- HTTP protocol *,
- HTTP Status *

## CRUD với Spring Rest
- Annotation:
    - `@RestController`,
    - `@RequestBody`,
    - `@PathVariable`
    - Spring Data Rest: auto CRUD simply
- HTTP method:
    - `@GetMapping`:
        - không truyền data ở body, truyền ở url (`@PathVariable`)
    - `@PostMapping`:
        - có thể truyền data ở body (`@RequestBody`)
    - `@PatchMapping`
        - applies partial modifications to a resource
    - `@PutMapping`
        - override a resource with the request payload
    - `@DeleteMapping`
- Format response:
    - API:
        - Header
        - Status
        - Body
    - `ResponseEntity`

