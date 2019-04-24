Refunds
*******

Refund Entity Definition
========================

Properties
----------

==================  ========  ======================================================================================
    Parameter       Type      Description
==================  ========  ======================================================================================
id                  Integer   Refund ID
order               Integer   Order ID
createdAt           Date      Creation time
updatedAt           Date      Update time
requester           String    Username of requester
requestReason       String    Reason of requester
responseReason      String    Reason of responder. e.g. The reason of rejection.
confirmedAt         Date      Refund confirm time
issuedAt            Date      Refund issue time
amount              Float     Amount
status              String    Status, one of ['PROCESSING', 'DENIED', 'ISSUED', 'CANCELLED', 'REQUESTED', 'WAITING']
trackingId          String    Shipment tracking id
carrierName         String    Carrier name
refundOnly          Boolean   If false, then customer should provide shipment infomation later
valid               Boolean   Valid flag
==================  ========  ======================================================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "order": 5,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "requester": "USERNAME_OF_REFUND_ISSUER",
     "requestReason": "I hate this product",
     "responseReason": null,
     "confirmedAt": null,
     "issuedAt": null,
     "amount": 500.0,
     "status": "REQUESTED",
     "trackingId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
     "carrierName": "SF-Express",
     "refundOnly": false,
     "valid": true
   }

Get Refunds Infomation
======================

This endpoint gets infomation of refunds.

HTTP Request
------------

``GET http://example.com/api/v2/refunds``

Request Parameters
------------------

=========== ======= ======== ======= ======================================================================================
Parameter   Type    Required Default Description
=========== ======= ======== ======= ======================================================================================
page        Integer False    0       The page index from 0
size        Integer False    20      Page size
orderId     String  False    -       Order ID
status      String  False    -       Status, one of ['PROCESSING', 'DENIED', 'ISSUED', 'CANCELLED', 'REQUESTED', 'WAITING']
valid       Boolean False    -       Valid flag
=========== ======= ======== ======= ======================================================================================

Response Parameters
-------------------
=========== ======== ===============================
Parameter   Type     Description
=========== ======== ===============================
data        Refund[] List of matching Refund objects
=========== ======== ===============================


.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API
   The requested orderId (if provided) must belong to you.

Get Infomation of a Specific Refund
===================================

This endpoint get infomation of a specific refund.

HTTP Request
------------

``GET http://example.com/api/v2/refunds/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Refund ID
========= ======== ===========

Response Parameters
-------------------
=========== ======== ===============================
Parameter   Type     Description
=========== ======== ===============================
data        Refund   The matching Refund object
=========== ======== ===============================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Create a Refund
===============

This endpoint creates a new refund.

HTTP Request
------------

``POST http://example.com/api/v2/refunds``

Request Parameters
------------------

==================  ========  ========  =======  ================================================================
    Parameter        Type     Required  Default  Description
==================  ========  ========  =======  ================================================================
orderId             Integer   True      -        ID of the order it belongs to
reason              String    True      -        Reason of this refund
amount              Float     True      -        Refund amount
refundOnly          Boolean   True      -        If false, then customer should provide tracking infomation later
==================  ========  ========  =======  ================================================================

Response Parameters
-------------------
=========== ======== ===================================
Parameter   Type     Description
=========== ======== ===================================
data        Refund   The created Refund object
=========== ======== ===================================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

Provide shipment infomation for a refund
========================================

This endpoint provides shipment infomation for a refund.

HTTP Request
------------

``PATCH http://example.com/api/v2/refunds/<id>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Refund ID
========= ======== ===========

Request Parameters
------------------

=========== ======= ======== ======= =======================
Parameter   Type    Required Default Description
=========== ======= ======== ======= =======================
action      String  True     -       Should be ``SHIP_INIT``
trackingId  String  True     -       Shipment tracking id
carrierName String  True     -       Carrier name
=========== ======= ======== ======= =======================

Response Parameters
-------------------
=========== ======== ==========================
Parameter   Type     Description
=========== ======== ==========================
data        Refund   The modified Refund object
=========== ======== ==========================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

   You can only provide shipment infomation for a refund whose ``refundOnly`` is ``false``

Cancel a Specific Refund
========================

This endpoint cancels a specific refund.

HTTP Request
------------

``DELETE http://example.com/api/v2/refunds/<ID>``

Path Parameter
--------------

========= ===========
Parameter Description
========= ===========
ID        Refund ID
========= ===========

.. Attention::
   Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API

   You can only cancel a refund which is in [ 'PENDING', 'WAITING', 'REQUESTED' ] status.
