# Trailers-Compose
A simple demo project for The Movie DB based on <b>MVVM clean architecture</b> and <b>Jetpack Compose</b>.

<img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/1.gif" width="200" style="max-width:100%;">  <img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/2.gif" width="200" style="max-width:100%;">  <img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/3.png" width="200" style="max-width:100%;">  <img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/4.png" width="200" style="max-width:100%;">  <img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/5.png" width="200" style="max-width:100%;">  <img src="https://github.com/anitaa1990/Trailers-Compose/blob/main/media/6.png" width="200" style="max-width:100%;"></br></br>

### App Features
* Users can view list of the movies from the TMDB database.
* Users can view list of the latest Tv series of their choice from the TMDB database.
* Users can filter movies based on popularity, upcoming top rated and now playing.
* Users can filter tv series based on popularity, airing today and top rated.
* Users can search for any movie or tv series of their choice.
* Users can click on any movie or tv series to watch the trailers of their choice.
* Supports pagination so you can literally view all movies/tv shows of your interest.

#### App Architecture 
Based on mvvm architecture and repository pattern.

<img src="https://github.com/anitaa1990/TrailersApp/blob/master/media/1.png" width="500" style="max-width:500%;">
 
 #### The app includes the following main components:

* A local database that servers as a single source of truth for data presented to the user. 
* A web api service.
* Pagination support for data received from the api.
* A repository that works with the database and the api service, providing a unified data interface.
* A ViewModel that provides data specific for the UI.
* The UI, using Jetpack Compose, which shows a visual representation of the data in the ViewModel.
* Unit Test cases for API service, Database, Repository and ViewModel.


#### App Packages
* <b>data</b> - contains 
    * <b>remote</b> - contains the api classes to make api calls to MovieDB server, using Retrofit. 
    * <b>local</b> - contains the db classes to cache network data.
    * <b>repository</b> - contains the repository classes, which acts as a bridge between the db, api and the paging classes.
    * <b>source</b> - contains the remote mediator and paging source classes, responsible for checking if data is available in the db and triggering api requests, if it is not, saving the response in the database.
* <b>module</b> - contains dependency injection classes, using Hilt.   
* <b>ui</b> - contains compose components and classes needed to display movie/tv list and movie/tv detail screen.
* <b>util</b> - contains util classes needed for compose redirection, ui/ux animations.


#### App Specs
* Minimum SDK 26
* Written in [Kotlin](https://kotlinlang.org/)
* MVVM Architecture
* Android Architecture Components (ViewModel, Room Persistence Library, Paging3 library, Navigation Component for Compose, DataStore)
* [Kotlin Coroutines]([url](https://kotlinlang.org/docs/coroutines-overview.html)) and [Kotlin Flows]([url](https://developer.android.com/kotlin/flow)).
* [Hilt]([url](https://developer.android.com/training/dependency-injection/hilt-android)) for dependency injection.
* [Retrofit 2](https://square.github.io/retrofit/) for API integration.
* [Gson](https://github.com/google/gson) for serialisation.
* [Okhhtp3](https://github.com/square/okhttp) for implementing interceptor, logging and mocking web server.
* [Mockito](https://site.mockito.org/) for implementing unit test cases
* [Coil]([url](https://coil-kt.github.io/coil/compose/)) for image loading.
* [Google Palette]([url](https://developer.android.com/develop/ui/views/graphics/palette-colors)): Jetpack library that extracts prominent colors from images to create visually engaging apps.
  
