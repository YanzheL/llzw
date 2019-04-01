Addresses
*********

Get a Specific Address
======================

This endpoint retrieves a specific address

HTTP Request
------------

``GET http://example.com/api/v1/addresses/<ID>``

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the address to retrieve
========= ======= ======== ======= =================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API.
    If you are ``SELLER``, then this address must belong to you or your customers.
    If you are ``CUSTOMER``, then this address must belong to you.

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "owner_id": "FOO",
       "province": "Beijing",
       "city": "Beijing",
       "district": "Haidian",
       "address": "No.XX XXX Road",
       "zip": "100000",
     }
   }

Get Addresses by Parameters
==============================

This endpoint retrieves all orders that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v1/addresses``

Request Parameters
------------------

Your should query with at least one parameter from this list.

========= ====== ======== ======= =====================
Parameter Type   Required Default Description
========= ====== ======== ======= =====================
owner_id  String False    -       Username of the owner
========= ====== ======== ======= =====================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API.
    If you are ``SELLER``, then this address must belong to you or your customers from your current orders.
    If you are ``CUSTOMER``, then this address must belong to you.

The response JSON structured like this, shows all addresses belongs to customer ``FOO``

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": [
       {
         "id": 1,
         "owner_id": "FOO",
         "province": "Beijing",
         "city": "Beijing",
         "district": "Haidian",
         "address": "No.XX XXX Road",
         "zip": "100000",
       }
     ]
   }

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
owner_id  String True     -       Username of the owner
province  String True     -       Province
city      String True     -       City
district  String True     -       District
address   String True     -       Detailed address
zip       String False    000000  Zip code
========= ====== ======== ======= =====================

..  Attention::
    Remember — You must be authenticated with the user you specified before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "owner_id": "FOO",
       "province": "Beijing",
       "city": "Beijing",
       "district": "Haidian",
       "address": "No.XX XXX Road",
       "zip": "100000",
     }
   }
