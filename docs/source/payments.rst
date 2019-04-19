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
order               Integer   Parent Order ID
createdAt           Date      Creation time
updatedAt           Date      Update time
payer               String    Username of payer
subject             String    Subject of this payment
description         String    Description
totalAmount         Float     Total amount
status              String    One of ['PENDING', 'CONFIRMED', 'TIMEOUT', 'INVALID']
confirmedAt         Date      Payment confirmation time
confirmed           Boolean   Whether the payment is confirmed by system
orderString         String    Alipay redirect URL
valid               Boolean   Valid flag
==================  ========  =====================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "order": 5,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "payer": "USERNAME_OF_CUSTOMER",
     "subject": "Macbook Pro 2019 32G 1TB",
     "description": null,
     "totalAmount": 500.00,
     "status": "PENDING",
     "confirmedAt": null,
     "confirmed": true,
     "orderString": "https://open.alipaydev.com/gateway.do?xxxxxxxxxx",
     "valid": true,
   }

Get Related Payments for an Order
=================================

This endpoint retrieves all related payments for an order.

HTTP Request
------------

``GET http://example.com/api/v2/payments``

Request Parameters
------------------

==================  ========  ========  =======  =============================
Parameter           Type      Required  Default  Description
==================  ========  ========  =======  =============================
orderId             Integer   True      -        Parent Order ID
==================  ========  ========  =======  =============================

Response Parameters
-------------------
=========== ========= ================================
Parameter   Type      Description
=========== ========= ================================
data        Payment[] List of matching Payment objects
=========== ========= ================================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

   The requested order must belongs to you.

Get a Specific Payment
======================

This endpoint retrieves a specific payment with id.

HTTP Request
------------

``GET http://example.com/api/v2/payments/<ID>``

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
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

   The requested payment must belongs to you.

Create a Payment
================

This endpoint creates a new payment.

HTTP Request
------------

``POST http://example.com/api/v2/payments``

Request Parameters
------------------

==================  ========  ========  =======  =============================
Parameter           Type      Required  Default  Description
==================  ========  ========  =======  =============================
orderId             Integer   True      -        Parent Order ID
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

``GET http://example.com/api/v2/payments/retry/<ID>``

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

