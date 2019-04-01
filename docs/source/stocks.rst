Stocks
******

Get Stocks by Parameters
===========================

This endpoint retrieves all stocks that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v1/stocks``

Request Parameters
------------------

Your should query with parameters from this list.

=========== ======= ======== ======= ===============================
Parameter   Type    Required Default Description
=========== ======= ======== ======= ===============================
page        Integer False    0       The page index from 0
size        Integer False    20      Page size
product_id  Integer False    -       ID of the product it belongs to
shelfLife   Integer False    -       Shelf-Life of the stock
trackingId  String  False    -       Tracking ID of this shipment
carrierName String  False    -       Carrier Name of this shipment
valid       Boolean False    -       The status of this stock
=========== ======= ======== ======= ===============================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

The response JSON structured like this, shows all stocks belongs to product ``2333``

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": [
       {
         "id": 1,
         "valid": true,
         "product_id": 2333,
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "producedAt": "2019-10-1 3:00 PM GMT+1:00",
         "inboundedAt": "2019-10-1 3:00 PM GMT+1:00",
         "shelfLife": 365,
         "totalQuantity": 10000,
         "currentQuantity": 6999,
         "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
         "carrierName": "SF-Express"
       },
       {
         "id": 2,
         "valid": true,
         "product_id": 2333,
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "producedAt": "2019-10-1 3:00 PM GMT+1:00",
         "inboundedAt": "2019-10-1 3:00 PM GMT+1:00",
         "shelfLife": 365,
         "totalQuantity": 20000,
         "currentQuantity": 7988,
         "trackingId": "",
         "carrierName": ""
       }
     ]
   }

Get a Specific Stock
====================

This endpoint retrieves a specific stock with id

HTTP Request
------------

``GET http://example.com/api/v1/stocks/<ID>``

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the product to retrieve
========= ======= ======== ======= =================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "valid": true,
       "product_id": 2333,
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "producedAt": "2019-10-1 3:00 PM GMT+1:00",
       "inboundedAt": "2019-10-1 3:00 PM GMT+1:00",
       "shelfLife": 365,
       "totalQuantity": 20000,
       "currentQuantity": 7988,
       "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
       "carrierName": "SF-Express"
     }
   }

Create a Stock
==============

This endpoint creates a new product.

HTTP Request
------------

``POST http://example.com/api/v1/stocks``

Request Parameters
------------------

============= ======= ======== ======= ===============================
Parameter     Type    Required Default Description
============= ======= ======== ======= ===============================
productId     String  True     -       ID of the product it belongs to
producedAt    String  True     -       The producing date
shelfLife     Integer True     -       Shelf-Life of this stock
totalQuantity Integer True     -       Total quantity of stock
trackingId    String  False    NULL    Tracking ID of this shipment
carrierName   String  False    NULL    Carrier Name of this shipment
============= ======= ======== ======= ===============================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "valid": true,
       "product_id": "ID_OF_THE_PRODUCT_IT_BELONGS_TO",
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "producedAt": "2019-10-1 3:00 PM GMT+1:00",
       "inboundedAt": "",
       "shelfLife": 365,
       "totalQuantity": 20000,
       "currentQuantity": 7988,
       "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
       "carrierName": "SF-Express"
     }
   }

Invalidate a Specific Stock
===========================

This endpoint invalidates a specific stock, so all items from this stock
will no longer be in the queue for sale.

It will NOT delete it from database.

HTTP Request
------------

``DELETE http://example.com/api/v1/stocks/<ID>``

Request Parameters
------------------

========= =================================
Parameter Description
========= =================================
ID        The ID of the stock to invalidate
========= =================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }
