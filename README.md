# Society Management App (Android)

A native Android application designed to simplify residential society management. The application provides a centralized platform for residents and administrators to manage society announcements, raise and track complaints, and access member information. It focuses on providing a simple, organized, and user-friendly experience for common society activities.

## Project Overview

The Society Management App is designed to simplify common activities in residential communities.  It provides a centralized platform where residents can view announcements, raise and track complaints, and access member information.  Administrator can manage announcements and update complaint statuses.  The application focuses on providing a simple and organized experience for both residents and administrators.

## Features Implemented

1. **Login / Registration with Roles** — Residents and Admins register with name, email, password, flat number, and phone. Passwords are hashed (SHA-256) before storage. Session persists across app restarts via DataStore, so users stay logged in.
2. **Digital Notice Board (Announcements)** — Admins can post society-wide announcements. All users can view them in a live-updating list. Admins can delete announcements they no longer need.
3. **Complaint / Service Request Management** — Residents raise complaints against categories (Plumbing, Electrical, Security, etc.). Residents see only their own complaints; Admins see and can update the status of every complaint (Open → In Progress → Resolved).
4. **Member Directory** — A browsable list of every registered resident and admin, with flat number and contact info.

Each feature includes proper **Loading**, **Empty**, and **Error** (with retry) states — not just the "happy path."

## Technology Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Navigation:** Navigation-Compose
- **Local Database:** Room
- **Session Persistence:** Jetpack DataStore (Preferences)
- **Concurrency:** Kotlin Coroutines + Flow
- **Min SDK:** 24, **Target SDK:** 34

## Architecture

**MVVM + Repository Pattern**

```
UI (Compose Screens)
      ↓ observes StateFlow
ViewModel (AuthViewModel, AnnouncementViewModel, ComplaintViewModel, DirectoryViewModel)
      ↓ calls suspend functions / collects Flow
Repository (AuthRepository, AnnouncementRepository, ComplaintRepository)
      ↓ reads/writes
Room Database (UserEntity, AnnouncementEntity, ComplaintEntity)
```

- **Separation of concerns:** UI never talks to the database directly — everything flows through a Repository, which is the single source of truth.
- **State management:** Every screen observes a `Resource<T>` sealed class (`Loading`, `Success`, `Error`, `Empty`) exposed as `StateFlow` from the ViewModel — this keeps state predictable and testable.
- **Dependency Injection:** A lightweight manual DI container (`SocietyApplication` + `ViewModelFactory`) wires repositories into ViewModels. A framework like Hilt was deliberately skipped for a project of this scope — it would add build complexity without meaningful benefit, and manual wiring is easier to explain and reason about in a walkthrough.
- **Navigation:** A single `NavHost` (`SocietyNavGraph`) drives all screen transitions, gated by role where relevant (e.g., only Admins see the "Post Announcement" FAB or complaint status controls).

## Application Flow

```
Login/Register → Home (role-based dashboard)
                    ├── Notice Board → Post Announcement (Admin only)
                    ├── Complaints   → Raise Complaint (Resident) / Update Status (Admin)
                    └── Member Directory
```

## Libraries Used

| Library | Purpose |
|---|---|
| Jetpack Compose + Material3 | Declarative UI |
| Navigation-Compose | In-app navigation |
| Room | Local persistence |
| DataStore Preferences | Session persistence |
| Kotlin Coroutines/Flow | Async data streams |
| KSP | Room annotation processing |

## Setup Instructions

1. Clone the repository:
   ```
   git clone <this-repo-url>
   ```
2. Open the project in **Android Studio (Koala or newer)**.
3. Let Gradle sync (it will download dependencies automatically — requires an internet connection).
4. Run on an emulator or physical device with **minSdk 24+**.
5. On first launch, register an account (choose "Admin" or "Resident") — there's no pre-seeded data, so registering is the starting point.

No backend, API keys, or Firebase setup is required — the app runs fully offline using Room.

## Known Limitations

- No password recovery flow (out of scope for a local-only auth system).
- Facility Booking, Visitor Management, and Polls/Surveys were intentionally **not** implemented, in favor of doing fewer features thoroughly.
- No automated tests included yet (see Future Improvements).
- Directory does not support search/filter — it's a straightforward list for now.
- Images/photos are not supported (e.g., no photo attachments on complaints).

## Future Improvements

- Add unit tests for ViewModels and repositories (using fake DAOs), and UI tests with Compose Testing.
- Add Facility Booking and Visitor Management as additional modules following the same MVVM pattern.
- Move to a remote backend (Firebase/Firestore) to support multi-device sync across a real society.
- Add push notifications for new announcements and complaint status changes.
- Add search and filtering to the Member Directory and Complaints list.


