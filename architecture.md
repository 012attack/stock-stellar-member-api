# architecture guide

## architecture
- hexagonal architecture
- domain model pattern

### layers
- Adapter Layer
- Domain Layer
- Application Layer

> exterior(Actor) → adapter → application → domain

## dto rule
- Single Responsibility Principle
- don't place request, response and other kind of concern like AuthDto.kt
- AuthDto.kt file must be separated to six classes

## required file's rules
- This is functions by supplied by our application
- This is used in interior adapter like api
- naming rule : object-oriented name 
example: MemberRegister.java, MemberFinder
bad case: domain + suffix is always service like AuthService

- There are only interior port interfaces in application/required package
- There are only implementation in application/{domain}/service
- There are only enums in application/{domain}/type

## provided file's rules
- This is exterior interface like DB, Cache, etc.
- There are only exterior interfaces in application/provided package

## Handling Exception rules
- use problemDetail object
- process similar exception by extending super class

## implementing service rules
- apply Command Query Responsibility Separation
  - separate prefix name like Command CommandMember
  - don't attach meaningless suffix name like handler

## implementing webapi controller rules
- separate file by purpose of controller, example: MemberRegisterApi, FindMemberApi, ...