Stocks
******

Stock Entity Definition
=======================

Properties
----------

==================  ========  ==============================
Parameter           Type      Description
==================  ========  ==============================
id                  Integer   Order ID
product_id          Integer   Parent product ID
createdAt           Date      Creation time
updatedAt           Date      Update time
producedAt          Date      Production time
inboundedAt         Date      Inbound time
shelfLife           Integer   Shelf-Life of this stock
totalQuantity       Integer   Total quantity of this stock
currentQuantity     Integer   Current quantity of this stock
trackingId          String    Shipment tracking id
carrierName         String    Carrier name
valid               Boolean   Valid flag
==================  ========  ==============================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "product_id": 2333,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "producedAt": "2019-10-1 3:00 PM GMT+1:00",
     "inboundedAt": "2019-10-1 3:00 PM GMT+1:00",
     "shelfLife": 365,
     "totalQuantity": 10000,
     "currentQuantity": 6999,
     "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
     "carrierName": "SF-Express",
     "valid": true
   }

Get Stocks by Parameters
===========================

This endpoint retrieves all stocks that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v2/stocks``

Request Parameters
------------------

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

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Stock[]   List of matching Stock objects
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Get a Specific Stock
====================

This endpoint retrieves a specific stock with id

HTTP Request
------------

``GET http://example.com/api/v2/stocks/<ID>``

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the product to retrieve
========= ======= ======== ======= =================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Stock     The matching Stock object
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

Create a Stock
==============

This endpoint creates a new product.

HTTP Request
------------

``POST http://example.com/api/v2/stocks``

Request Parameters
------------------

============= ======= ======== ======= ===============================
Parameter     Type    Required Default Description
============= ======= ======== ======= ===============================
productId     String  True     -       Parent product ID
producedAt    String  True     -       The producing date
shelfLife     Integer True     -       Shelf-Life of this stock
totalQuantity Integer True     -       Total quantity of stock
trackingId    String  False    null    Tracking ID of this shipment
carrierName   String  False    null    Carrier Name of this shipment
============= ======= ======== ======= ===============================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Stock     The created Stock object
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

Invalidate a Specific Stock
===========================

This endpoint invalidates a specific stock, so all items from this stock
will no longer be in the queue for sale.

It will NOT delete it from database.

HTTP Request
------------

``DELETE http://example.com/api/v2/stocks/<ID>``

Request Parameters
------------------

========= =================================
Parameter Description
========= =================================
ID        The ID of the stock to invalidate
========= =================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API
