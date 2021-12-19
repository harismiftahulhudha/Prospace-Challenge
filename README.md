# Prospace-Challenge

* [Project Directory Structure](#project_directory_structure)
    * [Database Directory](#database_directory)
        * [Dao Directory](#dao_directory)
    * [Di Directory](#di_directory)
    * [mvvm Directory](#mvvm_directory)
        * [Interfaces Directory](#interfaces_directory)
        * [Models Directory](#models_directory)
        * [Repositories Directory](#repositories_directory)
        * [Viewmodels Directory](#viewmodels_directory)
        * [Views Directory](#views_directory)
            * [Activities Directory](#activities_directory)
            * [Adapters Directory](#adapters_directory)
            * [Fragments Directory](#fragments_directory)
    * [Utils Directory](#utils_directory)
* [Resource Directory](#resource_directory)
    * [Drawable Directory](#drawable_directory)
    * [Layout Directory](#layout_directory)
    * [Menu Directory](#direktori_menu)
    * [Mipmap Directory](#mipmap_directory)
    * [Navigation Directory](#navigation_directory)
* [Variable Naming Format](#variable_naming_format)
* [Library](#project_libraries)

# <a name="project_directory_structure"></a>PROJECT DIRECTORY STRUCTURE
The following is a description of the function of each directory in this project.

## <a name="database_directory"></a>Database Directory
This directory is for classes related to local databases.<br/>
File naming format using **Camel Case**.

## <a name="dao_directory"></a>Database Directory > dao
This directory is intended for the **DATABASE ACCESS OBJECT** class whose function is to query tables.<br/>
for making class **dao** it is mandatory to use `Dao` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Dao**

## <a name="di_directory"></a>Di Directory
This directory is for classes related to dependency injection.<br/>
File naming format using **Camel Case**.
> className**Module**

## <a name="mvvm_directory"></a>mvvm Directory
This directory is for classes related to the **Model View ViewModel** architecture.

## <a name="interfaces_directory"></a>mvvm Directory > interfaces
This directory is intended for class interfaces as function callbacks on **User Interface**.<br/>
for making class **interfaces** it is required to use `Interface` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Model**

## <a name="models_directory"></a>mvvm Directory > models
This directory is intended for classes that are intended as object models.<br/>
for the creation of **models** classes, it is mandatory to use `Model` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Model**

## <a name="repositories_directory"></a>mvvm Directory > repositories
This directory is intended for data storage and retrieval logic classes **Model View ViewModel**.<br/>
for the creation of **repositories** classes, it is required to use `Repository` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Repository**

## <a name="viewmodels_directory"></a>mvvm Directory > viewmodels
This directory is intended for classes to pass data from **Repository** to **User Interface**.<br/>
for making class **viewmodels** it is required to use `ViewModel` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**ViewModel**

## <a name="views_directory"></a>mvvm Directory > views
This directory is for classes related **User Interface**.

## <a name="activities_directory"></a>mvvm Directory > views > activities
This directory is for the **Activity** class.<br/>
for the creation of **activities** classes, it is required to use `Activity` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Activity**

## <a name="adapters_directory"></a>mvvm Directory > views > adapters
This directory is for the **Adapter** class.<br/>
for making class **adapters** it is required to use `Adapter` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Adapter**

## <a name="fragments_directory"></a>mvvm Directory > views > fragments
This directory is for the **Fragment** class.<br/>
for making class **fragments** it is required to use `Fragment` at the end of the class name.<br/>
File naming format using **Camel Case**.
> className**Fragment**

## <a name="utils_directory"></a>Utils Directory
This directory is intended for utility classes related to **Generic Class and Configuration**.<br/>
File naming format using **Camel Case**.

# <a name="resource_directory"></a>RESOURCE DIRECTORY
The following describes the functionality of each directory in the project.

## <a name="drawable_directory"></a>Drawable Directory
This directory is intended for icons and xml files with selector functions.<br/>
File naming format using **Snake Case**.

## <a name="layout_directory"></a>Layout Directory
This directory is intended for xml files with user interface layout functions.
for xml creation it is mandatory to use `activity / fragment / dialog / item / custom / content / header` at the beginning of the xml name. And **_reference_function_layout**. <br/>
File naming format using **Snake Case**.
> **activity**_machine<br/>
**fragment**_machine<br/>
**dialog**_delete_machine<br/>
**item**_machine<br/>
**content**_body_machine<br/>
**header**_machine

## <a name="direktori_menu"></a>Menu Directory
This directory is intended for xml files with menu functions.
for making xml it is mandatory to use `menu` at the end of the xml name.<br/>
File naming format using **Snake Case**.
> main_**menu**

## <a name="mipmap_directory"></a>Mipmap Directory
This directory is intended for icon files.

## <a name="navigation_directory"></a>Navigation Directory
This directory is for xml files with navigation functions from the jetpack navigation component library.
for making xml it is mandatory to use `graph` at the end of the xml name.<br/>
File naming format using **Snake Case**.
> main_**graph**

# <a name="variable_naming_format"></a>VARIABLE NAMING FORMAT
Writing Format **VARIABLE** or **ID** `MANDATORY TO USE THE CAMEL CASE` FORMAT.<br/>
Special Writing **CONSTANT VARIABLE** `MANDATORY TO USE ALL CAPS FORMAT`.<br/>
For Global Creation of Protected Variables, please add them to `build.gradle`
```
debug {
   buildConfigField "String", "VARIABLE_NAME", "VALUES"
}
release {
   buildConfigField "String", "VARIABLE_NAME", "VALUES"
}
```
For Variable Calling in java Class
```
BuildConfig.VARIABLE_NAME
```

# <a name="project_libraries"></a>LIBRARY
The libraries I used to create this project.
```
LIFECYCLE + VIEW MODEL & LIVEDATA
ROOM
NAVIGATION COMPONENT
BARCODESCANNER
HILT
ZOOMAGE
```