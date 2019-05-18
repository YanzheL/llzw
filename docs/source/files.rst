Files
*****

File MetaData Definition
========================

Properties
----------

==================  ========  ======================================
Parameter           Type      Description
==================  ========  ======================================
id                  Integer   File ID
createdAt           Date      Creation time
hash                String    SHA-256 hash value of the file content
referrenceCount     Integer   Referrence Count of this file object
mimetype            String    MIME type of the file
==================  ========  ======================================

Allowed File Types
------------------

+-------------------------+-----------------+------------+
| Extension (Ignore case) | MIME Type       | Size Limit |
+=========================+=================+============+
| .jpg                    | image/jpeg      | 10M        |
+-------------------------+                 |            |
| .jpeg                   |                 |            |
+-------------------------+-----------------+            |
| .gif                    | image/gif       |            |
+-------------------------+-----------------+            |
| .png                    | image/png       |            |
+-------------------------+-----------------+            |
| .svg                    | image/svg+xml   |            |
+-------------------------+-----------------+------------+
| .wav                    | audio/wav       | 5M         |
+-------------------------+-----------------+            |
| .mp3                    | audio/mpeg      |            |
+-------------------------+-----------------+------------+
| .mp4                    | video/mp4       | 100M       |
+-------------------------+-----------------+            |
| .avi                    | video/x-msvideo |            |
+-------------------------+-----------------+            |
| .mpeg                   | video/mpeg      |            |
+-------------------------+-----------------+------------+
| .pdf                    | application/pdf | 20M        |
+-------------------------+-----------------+------------+

.. Attention::
   If a file has no referrence (``referrenceCount == 0``) 15 minutes after creation, it will be deleted from server.

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "hash": "1dd4984b0d118569da8620fe67e7fd4bd2889bb316d5ee40ba914eb65f19107d",
     "referrenceCount": 0,
     "mimetype": "image/jpeg"
   }

Upload a File
=============

This endpoint uploads a new file.

HTTP Request
------------

``POST http://example.com/api/v1/files``

Request Parameters
------------------

=========== ======== ======== ======= ================
Parameter   Type     Required Default Description
=========== ======== ======== ======= ================
file        File     True     -       The file object
=========== ======== ======== ======= ================

Response Parameters
-------------------
=========== ========= =======================================================
Parameter   Type      Description
=========== ========= =======================================================
data        FileMeta  The associated File MetaData object of the created file
=========== ========= =======================================================

.. Attention::
   Remember — You must be authenticated before using this API

Get a Specific File
===================

This endpoint provides a direct access to the requested file.

HTTP Request
------------

``GET http://example.com/api/v1/files/<HASH>``

Path Parameter
--------------

========= ======== ===========================================================================================================================
Parameter Required Description
========= ======== ===========================================================================================================================
HASH      True     The SHA-256 hash value of a file object in server, which should be the same as the hash value returned from File-Upload API
========= ======== ===========================================================================================================================

Delete a Specific File
======================

This endpoint decreases a file's ``referrenceCount``.

The file will be deleted from server once its ``referrenceCount`` reaches zero.

HTTP Request
------------

``DELETE http://example.com/api/v1/files/<HASH>``

Path Parameter
--------------

========= ======== ===========================================================================================================================
Parameter Required Description
========= ======== ===========================================================================================================================
HASH      True     The SHA-256 hash value of a file object in server, which should be the same as the hash value returned from File-Upload API
========= ======== ===========================================================================================================================
