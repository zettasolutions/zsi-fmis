    var accidents = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Accident");
        $(".panel-container").css("min-height", $(window).height() - 160);
        validations();
        $('#pao_id').select2({placeholder: "SELECT PAO",allowClear: true});
        $('#driver_id').select2({placeholder: "SELECT DRIVER",allowClear: true});
        $('#vehicle_id').select2({placeholder: "SELECT VEHICLE",allowClear: true});
        $('#gas_station').select2({placeholder: "SELECT GAS STATION",allowClear: true});
        //$("#client_phone_no").inputmask({"mask": "(99) 9999 - 9999"});
        $("#accident_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        $("#pms_type_id").dataBind({
            sqlCode      : "D235" //dd_pms_type_sel
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1]
                   pms_type_id     = isUD(_info) ? "" : _info.pms_type_id;
               
           }
        });
        $("#vehicle_id").dataBind({
            sqlCode      : "D231" //dd_vehicle_sel
           ,text         : "plate_no"
           ,value        : "vehicle_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1]
                   vehicle_id     = isUD(_info) ? "" : _info.vehicle_id;
               
           }
        });
        
        $("#driver_id").dataBind({
            sqlCode      : "D227" //drivers_sel
           ,text         : "full_name"
           ,value        : "user_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1]
                   _driver_id         = isUD(_info) ? "" : _info.user_id;
                //gDriverId = _driver_id;
           }
        });
        
        $("#pao_id").dataBind({
            sqlCode      : "P228" //pao_sel
           ,text         : "full_name"
           ,value        : "user_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1]
                   _driver_id         = isUD(_info) ? "" : _info.user_id;
                //gDriverId = _driver_id;
           }
        });
        
        $("#gas_station").dataBind({
             sqlCode    : "G215" // gas_station_sel
            ,text       : "gas_station_name"
            ,value      : "gas_station_id"
        });
        
        $("#accident_type_id").dataBind({
             sqlCode    : "D243" // dd_accident_types_sel
            ,text       : "accident_type"
            ,value      : "accident_type_id"
        });
        
        $("#error_type_id").dataBind({
             sqlCode    : "D244" // dd_error_types_sel
            ,text       : "error_type"
            ,value      : "error_type_id"
        });
        
    };
    
    $("#btnSaveAccident").click(function () {
        $("#formAccident").jsonSubmit({
             procedure: "accident_upd"
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert");
                   $("form").removeClass('was-validated');
                   $("#formAccident").find("input").val("");
                   $("#formAccident").find("textarea").val("");
                   $("#formAccident").find("select").val(null).trigger('change');
                   $("#myModal").find("#msg").text("Data successfully saved.");
                   $("#myModal").find("#msg").css("color","green");
                   setTimeout(function(){
                       $("#myModal").modal('toggle');
                       $("#accident_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
                       modalTxt();
                   },1000);
                }else{
                   $("#myModal").find("#msg").text("Something went wrong when saving the data.");
                   $("#myModal").find("#msg").css("color","red");
                }
            }
        }); 
    });
    
    function modalTxt(){
        setTimeout(function(){
           $("#myModal").find("#msg").text("Are you sure you want to save this data?");
           $("#myModal").find("#msg").css("color","#000");
        },1000);
    }

    function validations(){
        var forms = document.getElementsByClassName('needs-validation');
    	// Loop over them and prevent submission
    	var validation = Array.prototype.filter.call(forms, function(form) {
		form.addEventListener('submit', function(event) {
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

   
    
    return _public;
    
    
    
})();             