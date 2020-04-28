     var accidents = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Vehicle Insurance");
        $(".panel-container").css("min-height", $(window).height() - 160);
        validations();
        $('#vehicle_id').select2({placeholder: "SELECT VEHICLE",allowClear: true});
        $("#insurance_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        $("#expiry_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
        
        $("#vehicle_id").dataBind({
            sqlCode      : "D231" //dd_vehicle_sel
           ,text         : "plate_no"
           ,value        : "vehicle_id"
           ,onChange     : function(d){
               var _info           = d.data[d.index - 1]
                   vehicle_id     = isUD(_info) ? "" : _info.vehicle_id;
               
           }
        });
        
    };
    
    $("#btnSave").click(function () {
        $("#form").jsonSubmit({
             procedure: "vehicle_insurance_upd"
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert");
                   $("form").removeClass('was-validated');
                   $("#form").find("input").val("");
                   $("#form").find("textarea").val("");
                   $("#form").find("select").val(null).trigger('change');
                   $("#myModal").find("#msg").text("Data successfully saved.");
                   $("#myModal").find("#msg").css("color","green");
                   setTimeout(function(){
                       $("#myModal").modal('toggle');
                       $("#insurance_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
                       $("#expiry_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
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