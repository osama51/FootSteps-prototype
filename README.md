# FootSteps Prototype

## TODO

- [x] Add personalized reference stored with a dedicated screen
- [x] Add a scientific mode to the heatmap
- [x] Add curved-in/out bottom nav view
- [x] Add connected Bluetooth device to SharedPreferences
- [ ] Show available devices
- [ ] Add Save-Energy mode
- [ ] Handle phone's dark mode
- [x] Press back again to exit
- [ ] Add Stop button
- [ ] Create a custom view for the heatmap (mask + heatmap) (not necessary)

## Changes Log

### version-name: 1.0.1

* Checks if the device does not support bluetooth (helps with testing on emulators)
* Added the Room database for further utilization in analytical and reference parts of the app

* Designed and implemented base for the analysis raw data:
    * Storage Units:
        * LeftFootFrame
        * RightFootFrame
    * Storage Tables: One table for each foot
    * Interaction with database: (each table has its separate methods)
        * insert a frame
        * delete a frame
        * get all frames
        * get frames ordered by timestamp
        * get a certain frame by its corresponding timestamp
        * get a range of frames between two timestamps
        * get the last 50 frames
    * A singleton pattern is used to ensure it is only created once
    * A ViewModel is used to handle the data between the database and the UI (should be between the
      repository and the UI)

* Designed and implemented the reference database:
    * Storage Units:
        * User
    * Storage Tables: One table that holds all users
    * Interaction with database:
        * insert a user
        * delete a user
        * get all users
        * get users ordered by timestamp
        * get a certain use by title (name)
    * A singleton pattern is used to ensure it is only created once
    * A ViewModel is used to handle the data between the database and the UI (should be between the
      repository and the UI)

* Added a new screen for the reference data
    * The screen is a list in the form of a grid of all users
    * Each user has a name and a date of reference creation
    * The user can be selected by clicking on it
    * One user can be selected at a time
    * If no users exist, the app will ask the user to create one
    * One can add a new user by clicking on the floating action button
    * The screen is created as an activity and not a fragment, and that's not a good practice

* Implemented a testing heatmap that does not require external readings and can be run on emulators
    * The main purpose was to test the frame rate and how much of an effect the sending and receiving of data has on the frame rate.
    * I started with a few points in only one foot and that gave smooth animation of the heatmap, but once the points increased, even in just one foot, the frame rate started dropping badly and was returned to the usual ~20fps for around 9 points per foot.
    * A few tricks were tested to try and come up with best outcome possible, it was mainly focused on sending the biggest chunk of the processing to a back thread and only run what's required for displaying on the UI thread.
    * Four methods and manipulating them seemed to have great impact on how things went and whether or not the points rendered with the high fps:
      * invalidate()
      * forceRefresh()
      * postInvalidate()
      * forceRefreshOnWorkerThread()
    * The impact they had was not that big, but it was noticeable, and the best was the last two, which is what I went with.
    * Deciding which method to use was based on the following:
      * invalidate() and forceRefresh() are both called on the UI thread, which means that they will be called on the main thread, which is not what we want.
      * postInvalidate() causes an invalidate to happen on a subsequent cycle through the event loop, meaning it will be executed on the next frame. It's used to invalidate the View from a non-UI thread, which is what we want.
      * forceRefreshOnWorkerThread() draws the heatmap from a background thread. This allows offloading some of the work, but we'd have to take care to invalidate the view on the UI thread afterwards, "but not before this call has finished". which is what we want.
      * for some reason while testing, forceRefreshOnWorkerThread() worked better when called from the UI thread, otherwise it would give un-finished/rendered (shadowy with no colors) points. And even more weirdly, it worked better when called from the UI thread and the postInvalidate() was called from a background thread, which is the opposite of what the documentation says.
      * The best outcome was when forceRefreshOnWorkerThread() was called from the UI thread and postInvalidate() was called from a background thread, which is what I went with.
      * Until this point, the heatmap was being rendered with a frame rate of ~20fps, which is not that bad, but it can be better.

* Only a few days ago I took a deeper dive into the heatmap library and discovered some more attributes that are not included in the documentation, and they were the ones that made the biggest difference in the frame rate.
    * These points controlled how far the points borders are and how far the rendering goes (I think, apparently the rendering went beyond the view and that's why the frame rate was so low).
    * Playing around with these attributes, I was able to get the frame rate to *~50fps*, with the same number of points but for both feet, which is the best outcome possible.
    * This is not for sure since the testing uses a dummy data and thread sleep function to simulate the fps, but it's a good indicator (as compared to the same fps simulation methodology when had ~20fps) of how things will go when the real data is used.

* Currently, I'm reshaping the whole app, and shifting from activities to fragments which will affect how navigation is handled, and I'm trying to implement the MVVM architecture to the whole project (currently partially implemented), which is a big change and will take some time to finish.


#### To be done

* Implement repository pattern to handle the data: the role of the repository is to abstract the
  data layer from the rest of the app and provide a clean API for data access.
* The user should be updated by long pressing on it or selecting from the options menu
* Add the charts to a separate screen and add additional readings that are related to the user gait.
