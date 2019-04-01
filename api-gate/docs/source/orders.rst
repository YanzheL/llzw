Orders
******

Get Orders by Parameters
===========================

This endpoint retrieves all orders that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v1/orders``

Request Parameters
------------------

Your should query with at least one parameter from this list.

=========== ======= ======== ======= =================================
Parameter   Type    Required Default Description
=========== ======= ======== ======= =================================
page        Integer False    0       The page index from 0
size        Integer False    20      Page size
customer_id String  False    -       Username of the customer
address_id  Integer False    -       Destination address of this order
trackingId  String  False    -       Tracking ID of a shipped order
stock_id    Integer False    -       The stock ID it belongs to
=========== ======= ======== ======= =================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

The response JSON structured like this, shows all orders belongs to customer ``FOO``

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": [
       {
         "id": 1,
         "quantity": 2,
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "customer_id": "FOO",
         "address_id": 5,
         "stock_id": 23333,
         "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
         "carrierName": "SF-Express"
       },
       {
         "id": 2,
         "quantity": 5,
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "customer_id": "FOO",
         "address_id": 5,
         "stock_id": 12345,
         "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
         "carrierName": "SF-Express"
       },
     ]
   }

Get a Specific Order
====================

This endpoint retrieves a specific order

HTTP Request
------------

``GET http://example.com/api/v1/orders/<ID>``

Request Parameters
------------------

========= ======= ======== ======= ===============================
Parameter Type    Required Default Description
========= ======= ======== ======= ===============================
ID        Integer True     -       The ID of the order to retrieve
========= ======= ======== ======= ===============================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "quantity": 5,
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "customer_id": "USERNAME_OF_CUSTOMER",
       "address_id": 5,
       "stock_id": "ID_OF_THE_STOCK_IT_BELONGS_TO",
       "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
       "carrierName": "SF-Express"
     }
   }

Create an Order
===============

This endpoint creates a new order.

HTTP Request
------------

``POST http://example.com/api/v1/orders``

Request Parameters
------------------

=========== ======= ======== ======= ========================================
Parameter   Type    Required Default Description
=========== ======= ======== ======= ========================================
product_id  Integer True     -       ID of the product it belongs to
quantity    Integer True     -       Quantity of this order
customer_id String  True     -       Customer username who creates this order
address_id  Integer True     -       Destination address ID from address book
=========== ======= ======== ======= ========================================

..  Attention::
    Remember — You must be authenticated with ``CUSTOMER`` role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "quantity": 5,
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "customer_id": "USERNAME_OF_CUSTOMER",
       "address_id": 5,
       "stock_id": "ID_OF_THE_STOCK_IT_BELONGS_TO",
       "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
       "carrierName": "SF-Express"
     }
   }
