<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>Cassini Systems - Order <%= order.getOrderNumber() %></title>


    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.default.css">
    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.katniss.css">

    <style>
        body {
            background-color: #F2F7F9;
            padding: 20px !important;
        }
        .contentPanel {
            padding: 20px;
            background-color: #FFF;
            border: 1px solid #D0DDE1
        }

        .property-value {
            font-size: 16px;
        }

        .company-name {
            text-align: center;
            margin-bottom: 20px;
            margin-top: 20px;
        }

        .company-name div {
            font-size: 25px;
            height: auto;
            padding: 5px;
            border-radius: 20px;
            border: 5px solid #EEE;
            display: inline-block;
            line-height: 30px;
            font-weight: bold;
        }

        .company-address {
            font-size: 14px;
        }

        table { page-break-inside:auto }
        tr    { page-break-inside:avoid; page-break-after:auto }
        thead { display:table-header-group }
        tfoot { display:table-footer-group }

        table tr td {
            padding: 1px !important;
            font-size: 12px;
        }

        @media screen and (max-width: 800px) {
            body {
                background-color: #FFF;padding: 0px;
                font-size: 12px;
            }
            .contentPanel {
                padding: 20px;
                background-color: #FFF;
                border: 0;
            }
            .property-value {
                font-size: 12px;
            }
            .text-muted {
                font-size: 10px;
            }
        }

        @media print {
            body {
                background-color: #FFF;padding: 0px;
                font-size: 12px;
            }
            .contentPanel {
                padding: 10px;
                background-color: #FFF;
                border: 0;
            }
            .property-value {
                font-size: 12px;
            }
            .text-muted {
                font-size: 10px;
            }
            .company-name div {
                font-size: 15px;
            }
            h3 {
                font-size: 18px;
                font-family: 'Georgia', sans-serif;
                font-weight: bold;
            }
            h4 {
                font-size: 14px;
                margin: 5px;
            }

            table tr td {
                padding: 0px;
                font-size: 10px;
            }
            th {
                padding: 5px !important;
            }
            hr {
                margin: 0px;
            }
        }
    </style>

</head>
<body>
<div>
    <div class="row">
        <div class="contentPanel col-xs-12 col-md-10 col-md-offset-1">
            <div class="row">
                <div class="col-sm-12 company-name">
                    <div>APPLE BOOK COMPANY <br> DPPL & SCHOOL BOOK COMPANY</div>
                </div>
                <div class="text-center company-address">
                    Plot No: 2/B, C.F. Area, IDA Cherlapally, Phae-II, Hyderabad, Telangana - 500 051.<br>
                    Ph: 040 - 64579788, 65220941,65220942.  www.dppl.info
                </div>
            </div>
            <%
                def orderedDate = Date.parse("yyyy-MM-dd HH:mm:ss.SSS", order.orderedDate.toString());
                def orderedDateStr = orderedDate.format("dd-MM-yyyy, HH:mm:ss");
                def salesRep = order.customer.salesRep;
            %>

            <hr>
            <div class="row">
                <div class="col-xs-6">
                    <h4 style="text-decoration: underline dotted !important;">Bill To</h4>
                    <div>
                        <h4 style="margin:0">$order.customer.name</h4>
                        <span>$order.billingAddress.addressText</span><br>
                        <span>$order.billingAddress.city, $order.billingAddress.state.name - $order.billingAddress.pincode</span><br>
                        <span>Contact Phone: $order.contactPhone</span>
                    </div>
                </div>
                <div class="col-xs-6 text-right">
                    <span>DPPL Order Number: <strong>$order.orderNumber</strong></span> <br/>
                    <span>Date: <strong>$orderedDateStr</strong></span>

                    <%
                        if(order.verifications != null && order.verifications.size() > 0) {
                            def verification = order.verifications.iterator().next();
                    %>
                        <br>
                        <span>Assigned To: <strong>$verification.verifiedBy.firstName</strong></span>
                    <%  }
                    %>

                    <div>
                        <br/>
                        <h4 style="text-decoration: underline dotted !important;">Order Number</h4>
                        <h3 style="margin-top:0">$order.poNumber</h3>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <h4 style="text-decoration: underline dotted !important;">Ship To</h4>
                    <div>
                        <%
                            if(order.shipTo != null) {%>
                        <h4 style="margin: 0">$order.shipTo</h4>
                        <%}
                        else {%>
                        <h4 style="margin: 0">$order.customer.name</h4>
                        <%}
                        %>
                        <span>$order.shippingAddress.addressText</span><br>
                        <span>$order.shippingAddress.city, $order.shippingAddress.state.name - $order.shippingAddress.pincode</span><br>
                    </div>
                </div>
                <div class="col-xs-6 text-right">
                    <div>
                        <h4 style="text-decoration: underline dotted !important;">Reference</h4>
                        <span>$order.notes</span>
                        <%
                            if(salesRep != null) {
                        %>
                        <br>
                        <span>Sales Rep:  $salesRep.firstName</span>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <h3 class="text-center" style="margin: 0">$order.orderType ORDER</h3>
            <h3>Items</h3>
            <div class="row">
                <div class="col-xs-12">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th style="width: 50px;text-align: center;vertical-align: middle;">#</th>
                        <th style="vertical-align: middle;">Product</th>
                        <th style="vertical-align: middle;">SKU</th>
                        <th style="width: 50px;text-align: center;vertical-align: middle;">Qty</th>
                        <th style="width: 50px;text-align: center;vertical-align: middle;">Unit Price</th>
                        <th style="width: 50px;text-align: center;vertical-align: middle;">New Qty</th>
                        <th style="width: 50px;text-align: center;vertical-align: middle;">New Price</th>
                        </thead>
                        <tbody>
                        <%
                            for(i=0; i<order.details.size(); i++) {
                                def item = order.details[i];
                                def index = i + 1;
                                def balance = item.quantity;
                                if(item.quantityShipped != null) {
                                    balance = item.quantity - item.quantityShipped;
                                }

                                if(balance > 0) {
                        %>
                        <tr>
                            <td style="width: 50px;text-align: center;">$index</td>
                            <td>$item.product.name</td>
                            <td style="width: 50px;text-align: center;">$item.product.sku</td>
                            <td style="width: 75px;text-align: center;">$balance</td>
                            <td style="width: 75px;text-align: center;">${item.unitPrice.intValue()}</td>
                            <td style="width: 75px;text-align: center;"></td>
                            <td style="width: 75px;text-align: center;"></td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    window.focus();
    window.print();
</script>
</body>
</html>