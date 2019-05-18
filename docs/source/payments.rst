Payments
********

Payment Entity Definition
=========================

Properties
----------

==================  ========  =====================================================
Parameter           Type      Description
==================  ========  =====================================================
id                  Integer   Payment ID
order               String    Parent Order ID
orderString         String    Alipay redirect URL
createdAt           Date      Creation time
updatedAt           Date      Update time
payer               String    Username of payer
subject             String    Subject of this payment
description         String    Description
totalAmount         Float     Total amount
status              String    One of ['PENDING', 'CONFIRMED', 'TIMEOUT', 'INVALID']
vendorTradeId       String    Unique trade id from vendor.
confirmedAt         Date      Payment confirmation time
confirmed           Boolean   Whether the payment is confirmed by system
valid               Boolean   Valid flag
==================  ========  =====================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id" : 1,
     "order" : "c3beaaf0-ff02-4adf-b37c-ee41dbc20319",
     "orderString" : "https://openapi.alipaydev.com/gateway.do?XXX=XXX",
     "createdAt" : "2019-04-19T15:31:20.807+0000",
     "updatedAt" : "2019-04-19T15:31:20.807+0000",
     "payer" : "test_user_customer_username_0",
     "subject" : "Test Subject",
     "description" : "Test Description",
     "totalAmount" : 1000.123,
     "status" : "PENDING",
     "vendorTradeId" : null,
     "confirmedAt" : null,
     "confirmed" : false,
     "valid" : false
   }

Get Related Payments for an Order
=================================

This endpoint retrieves all related payments for an order.

HTTP Request
------------

``GET http://example.com/api/v1/payments``

Request Parameters
------------------

==================  ========  ========  =======  =============================
Parameter           Type      Required  Default  Description
==================  ========  ========  =======  =============================
orderId             String    True      -        Parent Order ID
==================  ========  ========  =======  =============================

Response Parameters
-------------------
=========== ========= ================================
Parameter   Type      Description
=========== ========= ================================
data        Payment[] List of matching Payment objects
=========== ========= ================================

.. Attention::
   Remember — You must be authenticated before using this API

   The requested order must belongs to you.

Get a Specific Payment
======================

This endpoint retrieves a specific payment with id.

HTTP Request
------------

``GET http://example.com/api/v1/payments/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Payment ID
========= ======== ===========

Response Parameters
-------------------
=========== ========= ================================
Parameter   Type      Description
=========== ========= ================================
data        Payment   The matching Payment object
=========== ========= ================================

.. Attention::
   Remember — You must be authenticated before using this API

   The requested payment must belongs to you.

Create a Payment
================

This endpoint creates a new payment.

HTTP Request
------------

``POST http://example.com/api/v1/payments``

Request Parameters
------------------

==================  ========  ========  =======  =============================
Parameter           Type      Required  Default  Description
==================  ========  ========  =======  =============================
orderId             String    True      -        Parent Order ID
subject             String    True      -        Subject of this payment
description         String    False     -        Description
==================  ========  ========  =======  =============================

Response Parameters
-------------------
=========== ========= ==============================
Parameter   Type      Description
=========== ========= ==============================
data        Payment   The created Payment object
=========== ========= ==============================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

   ``orderString`` will expire after 15 minutes.

Retry Payment Action
====================

This endpoint re-obtains ``orderString`` for a payment.

HTTP Request
------------

``GET http://example.com/api/v1/payments/retry/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Payment ID
========= ======== ===========

Response Parameters
-------------------
=========== ========= ================================================
Parameter   Type      Description
=========== ========= ================================================
data        Payment   The matching Payment object with new orderString
=========== ========= ================================================

Verify Payment
==============

This endpoint performs payment verifification triggered by client.

Usually, the system will verify a payment in three ways:

1. Receive asynchronous verification message posted by third-party payment vendor.

2. Query payment status positively as a scheduled task.

3. Perform verification asked by client.

HTTP Request
------------

``GET http://example.com/api/v1/payments/verify/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Payment ID
========= ======== ===========

Response Parameters
-------------------
=========== ========= ================================================
Parameter   Type      Description
=========== ========= ================================================
data        Boolean   The verification status
=========== ========= ================================================
