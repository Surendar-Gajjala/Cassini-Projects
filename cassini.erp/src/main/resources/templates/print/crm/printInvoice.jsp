<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>Cassini Systems</title>


    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.default.css">
    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.katniss.css">

    <style>
        .page-break {
            display: none;
        }
        body {
            background-color: #F2F7F9;
            padding: 20px !important;
            line-height: 15px;
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

        .header-table tr td, th {
            border: 1px solid #EEE;
            padding-left: 10px !important;
            vertical-align: top;
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

            .page-break {
                display: block;
                page-break-before: always;
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
<%
    for(def shipment : shipments) {
        def shipTo = shipment.order.shipTo != null ? shipment.order.shipTo : shipment.order.customer.name;
        def shippingAddress = shipment.shippingAddress;
        def invoiceDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(shipment.createdDate);
        def orderDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(shipment.order.orderedDate);
        def shipDate = new java.text.SimpleDateFormat("dd-MM-yyyy").format(shipment.consignment.shippedDate);
        def through = shipment.consignment.through != null ? shipment.consignment.through : "&nbsp;";
        def salesRep = shipment.order.customer.salesRep;
        def decimalFormat = new java.text.DecimalFormat("##,##,###.00");
%>
<div>
    <div class="row">
        <div class="contentPanel col-xs-12 col-md-10 col-md-offset-1">
            <div class="text-center">
                <strong>
                    Plot No: 2/B (C.F.Area) I.D.A Cherlapalli, Phase II, Hyderabad-51, Ph: 040 - 64579788, 65220941,65220942
                </strong>
            </div>
            <br>
            <h3 class="text-center">Delivery Challan</h3>
            <table style="width: 100%" class="header-table">
                <tr>
                    <td rowspan="3" width="60%">
                        <strong>Dachepalli Publishers Pvt. Ltd</strong><br>
                        APIIC Plot No: 2/B, C.F. Area, IDA Cherlapally, <br>
                        Phase-II, Hyderabad, Telangana - 500 051.<br>
                        Ph: 040 - 64579788, 65220941,65220942. <br>
                        www.dachepalli.com/www.dppl.info <br>
                        C.I.N: U22110AP1998PTC028994 <br>
                        E-Mail: vinod@dachepalli.com
                    </td>
                    <td width="20%">
                        Invoice No. <br>
                        <strong><%=shipment.invoiceNumber%></strong>
                    </td>
                    <td width="20%">
                        Dated <br>
                        <strong><%=invoiceDate%></strong>
                    </td>
                </tr>
                <tr>
                    <td width="20%">
                        Delivery Note <br>
                        <strong></strong>
                    </td>
                    <td width="20%">

                    </td>
                </tr>
                <tr>
                    <td width="20%">
                        Supplier's Ref. <br>
                        <strong></strong>
                    </td>
                    <td width="20%">
                        Other Reference(s) <br>
                        <strong>
                            <%
                                if(salesRep != null) {
                            %>
                            <span>Sales Rep:  $salesRep.firstName</span>
                            <%
                                }
                            %>
                        </strong>
                    </td>
                </tr>
                <tr>
                    <td rowspan="3" width="60%">
                        Buyer <br>
                        <strong><%= shipTo %></strong><br>
                        <%= shippingAddress.addressText %>
                    </td>
                    <td width="20%">
                        Buyer's Order No. <br>
                        <strong><%=shipment.order.poNumber%></strong>
                    </td>
                    <td width="20%">
                        Dated <br>
                        <strong><%=orderDate%></strong>
                    </td>
                </tr>
                <tr>
                    <td width="20%">
                        Dispatch Document No. <br>
                        <strong><%=shipment.consignment.confirmationNumber%></strong>
                    </td>
                    <td width="20%">
                        Dated <br>
                        <strong><%=shipDate%></strong>
                    </td>
                </tr>
                <tr>
                    <td width="20%">
                        Dispatched Through <br>
                        <strong><%=through%></strong>
                    </td>
                    <td width="20%">
                        Destination <br>
                        <strong></strong>
                    </td>
                </tr>
            </table>
            <table style="width: 100%" class="header-table">
                <tr>
                    <th style="width: 50px;">S. No.</th>
                    <th>Description of Goods</th>
                    <th>Quantity</th>
                    <th>Rate</th>
                    <th>Per</th>
                    <th>Amount</th>
                </tr>
                <%
                    def items = 0;
                    def invoiceTotal = 0;
                    for(int i=0; i<shipment.details.size(); i++){
                        def item = shipment.details.getAt(i);
                        if(item.quantityShipped != null) {
                            items += item.quantityShipped;
                        }
                        if(item.itemTotal != null) {
                            invoiceTotal += item.itemTotal;
                        }
                        def itemTotal = decimalFormat.format(item.itemTotal);
                %>
                <tr>
                    <td style="width: 50px;"><%=i+1%></td>
                    <td><%=item.product.name%></td>
                    <td><%=item.quantityShipped%> NO</td>
                    <td><%=item.unitPrice%></td>
                    <td>NO</td>
                    <td><%=itemTotal%></td>
                </tr>
                <%}
                %>
                <tr>
                    <td></td>
                    <td style="text-align: right">Total</td>
                    <td><strong><%=items%> NO</strong></td>
                    <td></td>
                    <td></td>
                    <td><strong>&#x20b9; <%=decimalFormat.format(invoiceTotal)%></strong></td>
                </tr>
            </table>

            <br>
            <div>
                <div class="row">
                    <div class="col-sm-4">
                        Company's VAT TIN
                    </div>
                    <div class="col-sm-4">
                        :  36160188951
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        Company's CST No.
                    </div>
                    <div class="col-sm-4">
                        :  36160188951
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        Company's PAN
                    </div>
                    <div class="col-sm-4">
                        :  AAACD7092C
                    </div>
                </div>
                <br>
                <div class="row">
                    <div class="col-sm-6">
                        Declaration <br>
                        We declare that this invoice shows the actual price of the goods described
                        and that all particulars are true and correct.
                    </div>
                    <div class="col-sm-6" style="border: 1px solid #EEE; text-align: right">
                        for Dachepalli Publishers Pvt. Ltd. <br><br>
                        Authorized Signatory
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<br><br><br>
<div class="page-break"></div>
<%}%>

<script>
    window.focus();
    window.print();
</script>
</body>
</html>