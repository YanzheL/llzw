Orders
******

Order Entity Definition
=======================

Properties
----------

==================  ========  =============================================
Parameter           Type      Description
==================  ========  =============================================
id                  Integer   Order ID
quantity            Integer   Quantity of this order
createdAt           Date      Creation time
updatedAt           Date      Update time
customerId          String    Customer username who creates this order
addressId           Integer   Destination address ID from address book
stockId             Integer   The actual stock id of this order
trackingId          String    Shipment tracking id
carrierName         String    Carrier name
totalAmount         Float     Total amount
remark              String    Addtional infomation of this order
shippingTime        Date      The time of shipping
deliveryConfirmed   Boolean   Whether the delivery is confirmed by customer
paid                Boolean   Whether the order is paid
valid               Boolean   Valid flag
==================  ========  =============================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "quantity": 2,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "customerId": "FOO",
     "addressId": 5,
     "stockId": 23333,
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

``GET http://example.com/api/v2/orders``

Request Parameters
------------------

=========== ======= ======== ======= =================================
Parameter   Type    Required Default Description
=========== ======= ======== ======= =================================
page        Integer False    0       The page index from 0
size        Integer False    20      Page size
customerId  String  False    -       Username of the customer
addressId   Integer False    -       Destination address of this order
trackingId  String  False    -       Tracking ID of a shipped order
stockId     Integer False    -       The stock ID it belongs to
valid       Boolean False    True    Valid flag
=========== ======= ======== ======= =================================

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order[]  List of matching Order objects
=========== ======== ==============================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Get a Specific Order
====================

This endpoint retrieves a specific order

HTTP Request
------------

``GET http://example.com/api/v2/orders/<ID>``

Request Parameters
------------------

========= ======= ======== ======= ===============================
Parameter Type    Required Default Description
========= ======= ======== ======= ===============================
ID        Integer True     -       The ID of the order to retrieve
========= ======= ======== ======= ===============================

Response Parameters
-------------------
=========== ======== ==============================
Parameter   Type     Description
=========== ======== ==============================
data        Order    The matching Order object
=========== ======== ==============================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Create an Order
===============

This endpoint creates a new order.

HTTP Request
------------

``POST http://example.com/api/v2/orders``

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

..  Attention::
    Remember — You must be authenticated with ``CUSTOMER`` role before using this API

