 
  var vehicleRepair = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle Repair");
        $(".panel-container").css("min-height", $(window).height() - 160);   
        validations();
        selects();
    };
    function selects(){
        $("#repair_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        $("#pms_type_id").dataBind({
            sqlCode      : "D235" //dd_pms_type_sel
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1];
               pms_type_id     = isUD(_info) ? "" : _info.pms_type_id; 
           }
        });
        $("#vehicle_id").dataBind({
            sqlCode      : "D231" //dd_vehicle_sel
           ,text         : "plate_no"
           ,value        : "vehicle_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1];
                vehicle_id     = isUD(_info) ? "" : _info.vehicle_id; 
           }
        });
    }
    function validations(){ 
        var forms = document.getElementsByClassName('needs-validation'); 
    	var validation = Array.prototype.filter.call(forms, function(form) {
    		form.addEventListener('submit', function(event) {
    		    $("form").removeClass('was-validated');
    			if (form.checkValidity() === false) {
    				event.preventDefault();
    				event.stopPropagation();
    			    $("form").addClass('was-validated');
    			}else{ 
        			event.preventDefault();
        			event.stopPropagation();
    			    $('#myModal').modal('show');
    			    $("form").addClass('was-validated');
    			}
    		}, false);
    	});
    }
    $("#btnSaveClient").click(function () {
        var _$frm = $("#formVehicleRepair"); 
        $("#formVehicleRepair").jsonSubmit({
             procedure: "vehicle_repairs_upd" 
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                   _$frm.find("input").val("");
                   _$frm.find("textarea").val(""); 
                   _$frm.find("select").val(null).trigger('change');
                   $("form").removeClass('was-validated');
                   $("#myModal").find("#msg").text("Data successfully saved.");
                   $("#myModal").find("#msg").css("color","green"); 
                   setTimeout(function(){
                       $("#myModal").modal('toggle');
                   },1000);
                }else{
                   $("#myModal").find("#msg").text("Something went wrong when saving the data.");
                   $("#myModal").find("#msg").css("color","red");
                }
            }
        }); 
    });
      
    return _public;
    
    
    
})();       









 