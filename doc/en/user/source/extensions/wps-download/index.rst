.. _wpsdownload:

WPS Download plugin
===================

WPS Download plugin provides some useful features for easily downloading:
* Raster or Vector layer as zip files
* Large maps as images
* Time based animation
The module also provides facilities to control the output file size.

Installing the WPS Download extension
-------------------------------------

The WPS Download extension is listed among the other extension downloads on the GeoServer download page.

The installation process is similar to other GeoServer extensions:

#. From the :website:`website download <download>` page, locate your release, and download: :download_extension:`wps-download`
   
   Verify that the version number in the filename corresponds to the version of GeoServer you are running (for example |release| above).

#. Extract the contents of the archive into the :file:`WEB-INF/lib` directory in GeoServer.
   Make sure you do not create any sub-directories during the extraction process.

#. Restart GeoServer.


Module description
------------------

This module provides the following WPS process:

 * ``gs:Download`` : can be used for downloading Raster and Vector Layers
 * ``gs:DownloadEstimator`` : can be used for checking if the downloaded file does not exceeds the configured limits.
 * ``gs:DownloadMap``: allows to download a large map with the same composition found on the client side (eventually along with an asynchronous call)
 * ``gs:DownloadAnimation``: allows to download a map with the same composition found on the client side, with animation over a give set of times
 
Configuring the limits
----------------------

The first step to reach for using this module is to create a new file called **download.properties** and save it in the GeoServer data directory. If the file is not present
GeoServer will automatically create a new one with the default properties:

 .. code-block:: xml
 
  # Max #of features
  maxFeatures=100000
  #8000 px X 8000 px
  rasterSizeLimits=64000000
  #8000 px X 8000 px X 3 bands X 1 byte per band = 192MB
  writeLimits=192000000
  # 50 MB
  hardOutputLimit=52428800
  # STORE =0, BEST =8
  compressionLevel=4
  # When set to 0 or below, no limit
  maxAnimationFrames=1000
  
Where the available limits are:

 * ``maxFeatures`` : maximum number of features to download
 * ``rasterSizeLimits`` : maximum pixel size of the Raster to read
 * ``writeLimits`` : maximum raw raster size in bytes (a limit of how much space can a raster take in memory). For a given raster, its raw size in bytes is calculated by multiplying pixel number (raster_width x raster_height) with the accumated sum of each band's pixel sample_type size in bytes, for all bands
 * ``hardOutputLimit`` : maximum file size to download
 * ``compressionLevel`` : compression level for the output zip file
 * ``maxAnimationFrames`` : maximum number of frames allowed (if no limit, the maximum execution time limits will still apply and stop the process in case there are too many)

.. note:: Note that limits can be changed when GeoServer is running. Periodically the server will reload the properties file. 
 
The processes and their usage
-----------------------------

The following describes the various processes, separating raw downloads from rendered downloads:

.. toctree::
   :maxdepth: 1

   rawDownload/
   mapAnimationDownload/
