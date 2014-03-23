<!doctype html>
<html lang="en-US">
  <head>
    <title>Forms</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="index.css">
  </head>
  <body>
    <!-- Create a basic form. -->

    <form id="dynamic_form" name="dynamic_form" action="getinfo" method="post">
      <fieldset>
        <legend>
          Basic Car Choice
        </legend>
        
        <div>
          <!-- This is an example of a select element. -->   
          <label for="dynamic_select">Make/Model:</label>
          <select name="autoModel" id="dynamic_select" >
            <jsp:include page="/getOption" flush="true"/>
            
          </select>
        </div>

        <div id="dynamic_option">
          <!-- append the dynamic content here! -->
        </div>  

        <script type="text/javascript" src="http://code.jquery.com/jquery-1.11.0.js"></script>
        <script>
          
          (function(win) {
            setTimeout(
              function(){
                $('#dynamic_select').change();
              },
              100
            );            
          })(window);

          $(function(){
            // bind change event to select
            $('#dynamic_select').bind('change', function () {
              var $auto=$('#dynamic_select').val();            
              $.get( "getinfo", {autoname:$auto}, function( data ) {
                $('#dynamic_option').html(data); 
              });


            });
          });
        </script>            

        <input type="submit" value="Done">
        
      </fieldset>
    
    </form>

  </body>
</html>




