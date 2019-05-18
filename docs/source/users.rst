Users
*****

Registration
============

This endpoint registers a new user.

HTTP Request
------------

``POST http://example.com/api/v1/users/register``

Request Parameters
------------------

=========== ======== ======== ======== ======================================================================================
Parameter   Type     Required Default  Description
=========== ======== ======== ======== ======================================================================================
username    String   True     -        Username with pattern [a-zA-Z]\w{5,50}
password    String   True     -        At least 1 upper case, 1 lower  case, 1 digit and 1 symbol, min length = 8
email       String   True     -        min length = 5, max length = 100
phoneNumber String   True     -        min length = 5, max length = 20
role        String   True     -        One of ['ROLE_SELLER', 'ROLE_CUSTOMER']
nickname    String   False    username Nickname, max length = 100.
avatar      String   False    -        Image URL of avatar, which can be external URL or hash value of an uploaded image file
=========== ======== ======== ======== ======================================================================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        User      The created User object
=========== ========= ===================================

Real Name Verification
======================

This endpoint provides real name verification details for a specific user.

HTTP Request
------------

``PUT http://example.com/api/v1/users/realNameVerification``

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

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        User      The modified User object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API

Update Password
===============

This endpoint updates a specific user’s password

HTTP Request
------------

``PUT http://example.com/api/v1/users/updatePassword``

Request Parameters
------------------

=========== ====== ======== ======= ================
Parameter   Type   Required Default Description
=========== ====== ======== ======= ================
oldPassword String True     -       Current password
newPassword String True     -       New password
=========== ====== ======== ======= ================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        User      The modified User object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API

Get Current User's Infomation
=============================

This endpoint retrieves a user’s infomation

HTTP Request
------------

``GET http://example.com/api/v1/users/me``

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        User      The current User object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API

Update Current User's Infomation
========================

This endpoint updates a user’s infomation

HTTP Request
------------

``PATCH http://example.com/api/v1/users/me``

Request Parameters
------------------

=========== ======== ======== ======== ======================================================================================
Parameter   Type     Required Default  Description
=========== ======== ======== ======== ======================================================================================
nickname    String   False    -        Nickname, max length = 100.
email       String   False    -        min length = 5, max length = 100
phoneNumber String   False    -        min length = 5, max length = 20
avatar      String   False    -        Image URL of avatar, which can be external URL or hash value of an uploaded image file
=========== ======== ======== ======== ======================================================================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        User      The modified User object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API
