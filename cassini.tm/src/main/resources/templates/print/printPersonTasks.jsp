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

    body {
      background-color: #F2F7F9;
      padding: 20px !important;
      line-height: 15px;
    }

    .header-table tr td, th {
      border: 1px solid #EEE;
      padding-left: 10px !important;
      vertical-align: top;
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
    }

    @media print {
      body {
        background-color: #FFF;padding: 0px;
        font-size: 12px;
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

  <h2><center>Person: <%=person%></center></h2>
  <h2><center>Date: <%=date%></center></h2>
</head>
<body>
<table class="table table-striped table-bordered">

  <thead>
  <tr>
    <th style="width: 200px;">Title</th>
    <th style="width: 200px;">Description</th>
    <th style="width: 150px;">Status</th>
    <th style="width: 150px;">Notes</th>
  </tr>

  </thead>
  <tbody>
  <tr ng-repeat="$task in $tasks">
    <td>$task.title</td>
    <td>$task.description</td>
    <td>$task.status</td>
    <td>$task.notes</td>
  </tr>
  </tbody>
</table>
<script>
  window.focus();
  window.print();
</script>
</body>
</html>