Addresses
*********

Address Entity Definition
=========================

Properties
----------

=========  ========  =====================
Parameter  Type      Description
=========  ========  =====================
id         Integer   Address ID
owner      String    Username of the owner
province   String    Province
city       String    City
district   String    District
address    String    Detailed address
zip        String    ZIP code
=========  ========  =====================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "owner": "FOO",
     "province": "Beijing",
     "city": "Beijing",
     "district": "Haidian",
     "address": "No.XX XXX Road",
     "zip": "100000",
   }

Get a Specific Address
======================

This endpoint retrieves a specific address

HTTP Request
------------

``GET http://example.com/api/v1/addresses/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Address ID
========= ======== ===========

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Address   The matching Address object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API.

   The requested address must belong to you.

Get Current User's Addresses
============================

This endpoint retrieves all addresses of current user.

HTTP Request
------------

``GET http://example.com/api/v1/addresses``

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Address[] List of matching Address objects
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated before using this API.

Create an Address
=================

This endpoint creates a new address.

HTTP Request
------------

``POST http://example.com/api/v1/addresses``

Request Parameters
------------------

========= ====== ======== ======= =====================
Parameter Type   Required Default Description
========= ====== ======== ======= =====================
province  String True     -       Province
city      String True     -       City
district  String True     -       District
address   String True     -       Detailed address
zip       String False    000000  Zip code
========= ====== ======== ======= =====================

.. Attention::
   Remember — You must be authenticated before using this API
