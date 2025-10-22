# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a TDD-based point management system built with Spring Boot. The project implements user point charging, usage, and history tracking functionalities with in-memory database tables that simulate network latency.

## Build System

**Gradle with Kotlin DSL** (build.gradle.kts)
- Java 17
- Spring Boot (via version catalog)
- Jacoco for code coverage

### Common Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Clean build artifacts
./gradlew clean

# Create executable JAR
./gradlew bootJar
```

## Architecture

### Layer Structure

**Controller → Service → Repository → Database Table**

The application follows a standard layered architecture with clear separation of concerns:

1. **Controller Layer** (`io.hhplus.tdd.point.controller`)
   - REST endpoints for point operations
   - PointController handles GET/PATCH requests for points and histories

2. **Service Layer** (`io.hhplus.tdd.point.service`)
   - Business logic implementation
   - UserPointService: point charging/usage operations
   - PointHistoryService: history recording operations

3. **Repository Layer** (`io.hhplus.tdd.point.repository`)
   - Data access abstraction
   - Interface + Implementation pattern (e.g., UserPointRepository / UserPointRepositoryImpl)

4. **Database Tables** (`io.hhplus.tdd.database`)
   - In-memory storage with simulated latency (200-300ms random delays)
   - **DO NOT MODIFY**: UserPointTable and PointHistoryTable classes
   - Only use their public APIs

### Domain Model

**Domain objects are immutable records:**
- `UserPoint`: Uses record with immutability - `chargePoint()` and `usePoint()` return new instances
- `PointHistory`: Immutable transaction record
- TransactionType enum: CHARGE, USE

### Exception Handling

**Custom exception hierarchy:**
- Base: `BusinessException` (extends RuntimeException)
- Domain exceptions: `UserNotFoundException`, `PointRangeException`
- ErrorCode enum: Centralized error messages with HTTP status codes
- Global handler: `ApiControllerAdvice` with `@RestControllerAdvice`

**Error codes:**
- USER_NOT_FOUND (404): User doesn't exist
- USER_POINT_MUST_POSITIVE (400): Negative amount provided
- USER_POINT_NOT_ENOUGH (400): Insufficient balance
- USER_POINT_OVERFLOW (400): Amount exceeds Long.MAX_VALUE

### Key Design Patterns

1. **Repository Pattern**: Abstract data access behind interfaces
2. **DTO Pattern**: Separate request/response objects from domain entities
   - Request DTOs: PointChargeDTO, PointUseDTO
   - Response DTOs: UserPointDTO, PointHistoryDTO
3. **Service Interface Pattern**: UserPointService/UserPointServiceImpl separation
4. **Immutability**: Domain records return new instances on state changes

## Important Constraints

1. **Database Table Classes**: UserPointTable and PointHistoryTable must NOT be modified. Use only their public APIs.

2. **Throttling**: Database operations include random delays (200-300ms) to simulate real network conditions.

3. **Point Validation**: Business logic in UserPoint domain validates:
   - Positive amounts only
   - Overflow protection using Math.addExact/subtractExact
   - Sufficient balance for usage

4. **Transaction Atomicity**: Point operations (charge/use) create both UserPoint updates AND PointHistory entries - ensure both succeed or fail together.

## Testing Notes

- Tests use JUnit Platform (configured in build.gradle.kts)
- Test failures don't break the build (ignoreFailures = true)
- Jacoco version 0.8.7 for coverage reporting