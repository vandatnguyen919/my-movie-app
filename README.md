<h1 align="center">MyMovieApp</h1>

<p align="center">
MyMovieApp is a sample Android project using <a href="https://www.themoviedb.org/">The Movie DB</a> API based on MVVM architecture. It showcases the latest Android tech stacks with well-designed architecture and best practices.

<img src='art/img.png' width='25%'/><img src = 'art/img_1.png' width='25%'/><img src='art/img_2.png' width='25%'/><img src ='art/img_3.png' width='25%'/>

</p>

## Features
* 100% Java
* MVVM architecture
* Reactive pattern
* Android Architecture Components and Jetpack Library.
* Single activity pattern
* Dependency injection

<img src="./art/img_4.png" align="right" width="32%" alt=""/>

## Tech Stacks
* [Retrofit](http://square.github.io/retrofit/) + [OkHttp](http://square.github.io/okhttp/) - RESTful API and networking client.
* [Dagger](https://github.com/google/dagger) - Dependency injection.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collections of libraries that help you design robust, testable and maintainable apps.
    * [Room](https://developer.android.com/training/data-storage/room) - Local persistence database.
    * [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Pagination loading for RecyclerView.
    * [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) - UI related data holder, lifecycle aware.
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Observable data holder that notify views when underlying data changes.
    * [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declarative way to bind data to UI layout.
    * [Navigation component](https://developer.android.com/guide/navigation) - Fragment routing handler. (Upcoming)
    * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Tasks scheduler in background jobs. (Upcoming)
* [RxJava](https://github.com/ReactiveX/RxJava) - Asynchronous programming with observable streams.
* [Glide](https://github.com/bumptech/glide) - Image loading.
* [Firebase](https://firebase.google.com/) - Backend services and storage for mobile apps.

## Architectures

![MVVM](./art/MyMovieApp_Architecture.png)

I follow Google recommended [Guide to app architecture](https://developer.android.com/jetpack/guide) to structure our architecture based on MVVM, reactive UI using LiveData / RxJava observables and data binding.

* **View**: Activity/Fragment with UI-specific logics only.
* **ViewModel**: It keeps the logic away from View layer, provides data streams for UI and handle user interactions.
* **Model**: Repository pattern, data layers that provide interface to manipulate data from both the local and remote data sources. The local data sources will serve as [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth).

## Package Structures

Data Module: Data modeling and access layer
```
com.example.data        # Root Package
├── mapper              # Convert data between data layer and domain layer.
├── repository          # Repository pattern combining local and remote data.
└── source              # Data source for local and remote.
    ├── local           # Local persistence database
    │   ├── dao         # Data Access Objects for Room database
    │   └── entity      # Entity classes for Room database tables
    └── remote          # Remote data source
        ├── model       # Data models for network responses
        ├── paging      # Paging sources for large data sets
        └── service     # Retrofit services for API calls
```

Domain Module: Core business logic and use cases
```
com.example.domain      # Root Package
├── model               # Business models used across the app
├── repository          # Interfaces for repository implementations
├── usecase             # Use cases for handling business logic
│  └── base             # Base use cases for common logic
└── utils               # Utility classes for domain-specific operations
```

Presentation Module: User interface and ViewModel layer
```
com.example.mymovieapp  # Root Package
├── di                  # Dependency injection setup using Dagger
├── listener            # Interface callbacks for UI interactions
├── ui                  # User interface screens and components
│   ├── about           # About screen UI
│   ├── adapters        # Adapters for RecyclerViews and other lists
│   ├── base            # Base classes for common UI logic like movies and favorites list fragments
│   ├── favorites       # Favorites screen UI
│   ├── main            # Main navigation and layout
│   ├── movies          # Movie-related UI components
│   │   ├── details     # Details screen for movies
│   │   └── list        # List screen for movies
│   ├── profile         # Profile screen UI
│   ├── reminder        # Reminder management UI
│   ├── settings        # Settings screen UI
│   └── MainActivity    # Single activity hosting fragments
├── utils               # Utility classes for presentation-specific operations
└── workers             # Background tasks and WorkManager workers
```

## LICENSE

```
Copyright (c) 2020 Engine Bai

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

