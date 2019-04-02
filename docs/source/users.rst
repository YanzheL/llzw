Users
*****

Registration
============

The response JSON structured like this

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }

This endpoint registers a new user.

HTTP Request
------------

``POST http://example.com/api/v2/users/register``

Request Parameters
------------------

=========== ======== ======== ======= ==========================================================================
Parameter   Type     Required Default Description
=========== ======== ======== ======= ==========================================================================
username    String   True     -       Username
password    String   True     -       At least 1 upper case, 1 lower  case, 1 digit and 1 symbol, min length = 8
email       String   True     -       min length = 5, max length = 100
phoneNumber String   True     -       min length = 5, max length = 20
role        String   True     -       One of ['ROLE_SELLER', 'ROLE_CUSTOMER']
=========== ======== ======== ======= ==========================================================================

Real Name Verification
======================

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }

This endpoint provides real name verification details for a specific
user.

HTTP Request
------------

``PUT http://example.com/api/v2/users/realNameVerification``

Request Parameters
------------------

============== ======== ======== ======= =========================================================
Parameter      Type     Required Default Description
============== ======== ======== ======= =========================================================
identityType   String   True     -       One of ['PRC_ID', 'PASSPORT']
identityNumber String   True     -       ID of specified document, min length = 1, max length = 20
firstName      String   True     -       min length = 1, max length = 20
lastName       String   True     -       min length = 1, max length = 20
============== ======== ======== ======= =========================================================

..  Attention::
    Remember — You must be authenticated before using this API

Update Password
===============

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }

This endpoint updates a specific user’s password

HTTP Request
------------

``PUT http://example.com/api/v2/users/updatePassword``

Request Parameters
------------------

=========== ====== ======== ======= ================
Parameter   Type   Required Default Description
=========== ====== ======== ======= ================
oldPassword String True     -       Current password
newPassword String True     -       New password
=========== ====== ======== ======= ================

..  Attention::
    Remember — You must be authenticated before using this API