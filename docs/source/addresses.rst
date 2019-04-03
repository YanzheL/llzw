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
ownerId   String    Username of the owner
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
     "ownerId": "FOO",
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

``GET http://example.com/api/v2/addresses/<ID>``

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the address to retrieve
========= ======= ======== ======= =================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Address   The matching Address object
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API.
    If you are ``SELLER``, then this address must belong to you or your customers.
    If you are ``CUSTOMER``, then this address must belong to you.

Get Addresses by Parameters
==============================

This endpoint retrieves all orders that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v2/addresses``

Request Parameters
------------------

Your should query with at least one parameter from this list.

========= ====== ======== ======= =====================
Parameter Type   Required Default Description
========= ====== ======== ======= =====================
ownerId  String False    -       Username of the owner
========= ====== ======== ======= =====================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Address[] List of matching Address objects
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API.
    If you are ``SELLER``, then this address must belong to you or your customers from your current orders.
    If you are ``CUSTOMER``, then this address must belong to you.

Create an Address
=================

This endpoint creates a new address.

HTTP Request
------------

``POST http://example.com/api/v2/addresses``

Request Parameters
------------------

========= ====== ======== ======= =====================
Parameter Type   Required Default Description
========= ====== ======== ======= =====================
ownerId  String True     -       Username of the owner
province  String True     -       Province
city      String True     -       City
district  String True     -       District
address   String True     -       Detailed address
zip       String False    000000  Zip code
========= ====== ======== ======= =====================

..  Attention::
    Remember — You must be authenticated with the user you specified before using this API

