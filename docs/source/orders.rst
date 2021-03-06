Orders
******

Order Entity Definition
=======================

Properties
----------

================== =========== =============================================
Parameter          Type        Description
================== =========== =============================================
id                 String      Order ID
quantity           Integer     Quantity of this order
createdAt          Date        Creation time
updatedAt          Date        Update time
customer           String      Customer username who creates this order
address            AddressBean Associated Address object of this order
product            Integer     Parent product id
stocks             Integer[]   The related stocks of this order
trackingId         String      Shipment tracking id
carrierName        String      Carrier name
totalAmount        Float       Total amount
remark             String      Addtional infomation of this order
shippingTime       Date        The time of shipping
deliveryConfirmed  Boolean     Whether the delivery is confirmed by customer
paid               Boolean     Whether the order is paid
valid              Boolean     Valid flag
================== =========== =============================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": "c3beaaf0-ff02-4adf-b37c-ee41dbc20319",
     "quantity": 2,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "customer": "FOO",
     "address": {
       "province": "Beijing",
       "city": "Beijing",
       "district": "Haidian",
       "address": "Jianguo Avenue",
       "zip": "100000"
     },
     "product": 1,
     "stocks": [11, 12, 13, 14, 15],
     "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
     "carrierName": "SF-Express",
     "totalAmount": 500.00,
     "remark": null,
     "shippingTime": "2019-10-1 3:00 PM GMT+1:00",
     "deliveryConfirmed": false,
     "paid": false,
     "valid": true
   }

Get Orders by Parameters
========================

This endpoint retrieves all orders that satisfy given parameters

HTTP Request
------------

``GET http://example.com/api/v1/orders``

Request Parameters
------------------

================= ======= ======== ======= =============================================
Parameter         Type    Required Default Description
================= ======= ======== ======= =============================================
page              Integer False    0       The page index from 0
size              Integer False    20      Page size
customerId        String  False    -       Username of the customer
trackingId        String  False    -       Tracking ID of a shipped order
stockId           Integer False    -       The stock ID it belongs to
valid             Boolean False    -       Valid flag
deliveryConfirmed Boolean False    -       Whether the delivery is confirmed by customer
paid              Boolean False    -       Whether the order is paid
================= ======= ======== ======= =============================================

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order[]  List of matching Order objects
=========== ======== ==============================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Get a Specific Order
====================

This endpoint retrieves a specific order

HTTP Request
------------

``GET http://example.com/api/v1/orders/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Order ID
========= ======== ===========

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order    The matching Order object
=========== ======== ==============================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

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
productId   Integer True     -       ID of the product it belongs to
quantity    Integer True     -       Quantity of this order
customerId  String  True     -       Customer username who creates this order
addressId   Integer True     -       Destination address ID from address book
remark      String  False    -       Remark
=========== ======= ======== ======= ========================================

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order    The created Order object
=========== ======== ==============================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

Cancel a Specific Order
=======================

This endpoint cancels a specific order.

HTTP Request
------------

``DELETE http://example.com/api/v1/orders/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Order ID
========= ======== ===========

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

   You can only cancel an order which has not been shipped.

Delivery Confirmation
=====================

This endpoint confirms delivery of an order.

HTTP Request
------------

``PATCH http://example.com/api/v1/orders/<ID>/DELIVERY_CONFIRM``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Order ID
========= ======== ===========

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order    The modified Order object
=========== ======== ==============================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

Update an Order
===============

This endpoint updates shipment details of an order.

HTTP Request
------------

``PATCH http://example.com/api/v1/orders/<ID>/SHIP``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Order ID
========= ======== ===========

Request Parameters
------------------

============ ======= ======== ======= ========================================
Parameter    Type    Required Default Description
============ ======= ======== ======= ========================================
trackingId   String  True     -       Tracking ID from shipment carrier
carrierName  String  True     -       Shipment carrier name
shippingTime String  True     -       Time of shipping
============ ======= ======== ======= ========================================

.. Note::
   Example date string format: ``"2000-10-31T01:30:00.000-05:00"``

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order    The modified Order object
=========== ======== ==============================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` role before using this API
