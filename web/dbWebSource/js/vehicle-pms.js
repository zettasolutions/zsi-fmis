  var pms = (function(){
    var  _pub = {}
        ,gPMSid  = ""
        ,gTotal = 0.00
    ;
    zsi.ready = function(){
        $(".page-title").html("Preventive Maintenance");
        $(".panel-container").css("min-height", $(window).height() - 160);
        dispalyReplacementParts();
        markPMMandatory();
        displaySelects();
    };
    
    function displaySelects(){
        $("#pms_date").datepicker({todayHighlight:true ,endDate:new Date()}).datepicker("setDate","0");
        $('#vehicle_id').select2({placeholder: "",allowClear: true});  
        $("#vehicle_id").dataBind({
            sqlCode      : "D272"
           ,parameters   : {client_id:app.userInfo.company_id}
           ,text         : "vehicle_plate_no"
           ,value        : "vehicle_id"
        });
        $("#status_id").dataBind({
            sqlCode      : "S122"
           ,text         : "status_name"
           ,value        : "status_id"
        });
        $("#pm_amount").attr("readonly", true);
        $('#service_amount').maskMoney();
    }
    
    function dispalyReplacementParts(){
        var _getData = function(cb){
            var _rows = [];
            if(gPMSid!==""){
                zsi.getData({
                     sqlCode    : "P249"
                    ,parameters : {pms_id: gPMSid}
                    ,onComplete : function(d) {
                        _rows = d.rows;
                        cb(_rows);
                    }
                });
            }else cb(_rows);
        };
        
        _getData(function(rows){
            var _rows = rows;
            var _seqNo = -1;
            var _rowTotal = {
                replacement_id : ""
                ,pms_id : ""
                ,repair_id : ""
                ,seq_no : ""
                ,part_id : ""
                ,part_qty : ""
                ,unit_id : ""
                ,unit_cost : "Overall Cost"
                ,total_cost : 0.00
                ,is_replacement : ""
                ,is_bnew : ""
            }; 
            _rows.push(_rowTotal);
            
            $("#gridReplacementParts").dataBind({
                 rows: _rows
                ,height : $(window).height() - 500
                ,blankRowsLimit : 10
                ,dataRows : [
                    {text: "Item No.", width: 60
                        ,onRender : function(d){ 
                            _seqNo++;
                            return app.bs({type: "hidden", name: "replacement_id", value: app.svn(d,"replacement_id")})
                                +  app.bs({type: "hidden", name: "is_edited"})  
                                +  app.bs({type: "hidden", name: "pms_id", value: gPMSid})  
                                +  app.bs({type: "hidden", name: "repair_id"})  
                                +  app.bs({type: "input",  name: "seq_no", value: (app.svn(d,"seq_no") ? app.svn(d,"seq_no") : (_seqNo > 0) ? _seqNo : ""), style: "text-align:center"}); 
                        }
                    }
                    ,{text: "Part", type: "select", name: "part_id", width: 130, style: "text-align:left"}
                    ,{text: "Part Qty", width: 120
                        ,onRender : function(d){ 
                            return app.bs({type: "input",  name: "part_qty", value: app.svn(d,"part_qty")       , style: "text-align:center"});
                        }
                    }
                    ,{text: "Unit", type: "select", name: "unit_id", width: 130, style: "text-align:left"}
                    ,{text: "PM Cost", width: 120
                        ,onRender : function(d){ 
                            var _unitCost = app.svn(d,"unit_cost");
                            if(_unitCost==="Overall Cost"){
                                return "<b class='d-block px-1 text-white text-right'>"+ _unitCost +"</b>";
                            }else return app.bs({type: "input"      ,name: "unit_cost"      ,value: _unitCost.toCommaSeparatedDecimal()     ,style: "text-align:right"});
                        }
                    }
                    ,{text: "Total Cost", width: 120
                        ,onRender : function(d){ 
                            var _partQty = app.svn(d,"part_qty");
                            var _unitCost = app.svn(d,"unit_cost");
                            var _totalCost = (parseFloat(_partQty).toFixed(2) * parseFloat(_unitCost).toFixed(2));
                            if(_unitCost==="Overall Cost"){
                                return "<b id='total' class='d-block px-1 text-white text-right'>"+ gTotal.toCommaSeparatedDecimal() +"</b>";
                            }else return app.bs({type: "input"      ,name: "total_cost"     ,value: _totalCost.toCommaSeparatedDecimal()      ,style: "text-align:right"});
                        }
                    }
                    ,{text: "Replaced (Yes/No)"         ,type: "yesno"          ,name: "is_replacement"     ,width: 120     ,style: "text-align:center"     ,defaultValue:"N"}
                    ,{text: "New (Yes/No)"              ,type: "yesno"          ,name: "is_bnew"            ,width: 100     ,style: "text-align:center"     ,defaultValue:"N"}
                ]
                ,onComplete : function(o){
                    markReplacementPartsMandatory();
                    $(".zRow:contains('Overall Cost')").addClass("zTotal");
                    $(".zRow:contains('Overall Cost')").find("[name='part_id'],[name='part_qty'],[name='unit_id'],[name='is_replacement'],[name='is_bnew']").remove();
                    var _$grid = this;
                        
                    _$grid.find("[name='part_id']").dataBind({
                        sqlCode      : "D256"
                       ,text         : "part_desc"
                       ,value        : "part_id"
                    });
                    _$grid.find("[name='unit_id']").dataBind({
                        sqlCode      : "D257"
                       ,text         : "unit_name"
                       ,value        : "unit_id"
                    });
                    
                    function total(){
                        var _serviceAmt = $("#service_amount").val().replace(/,/g, "");
                        var _totalPmsAmt = $("#total_pms_amount");
                        var _$lastRow = $(".zRow:contains('Overall Cost')").find("#total");
                        var _totalCost = $(".zRow:not(:contains('Overall Cost'))").find("[name='total_cost']");
                        var _data = [];
                        var _totality = 0.00;
                        
                        _totalCost.each(function(){
                            if(this.value) _data.push(this.value.replace(/,/g, ""));
                        });
                        
                        for (var i = 0; i < _data.length; i++){
                           _totality += parseFloat(_data[i]);
                        }
                        
                        gTotal = _totality;
                        
                        _totalPmsAmt.val((_totality + parseFloat(_serviceAmt)).toCommaSeparatedDecimal());
                       _$lastRow.text(_totality.toCommaSeparatedDecimal());
                    }
                    
                    _$grid.find("[name='part_qty'],[name='unit_cost']").on("keyup change",function(){
                        var _$row = $(this).closest(".zRow");
                        var _$totalCost = _$row.find("[name='total_cost']")
                            ,_$partQty = _$row.find("[name='part_qty']")
                            ,_$unitCost = _$row.find("[name='unit_cost']")
                            ,_partQty = _$partQty.val().replace(/,/g, "")
                            ,_unitCost = _$unitCost.val().replace(/,/g, "")
                            ,_totalCost = "";
                            
                            if(_partQty!=="" && _unitCost!==""){
                                _totalCost = parseFloat(_partQty).toFixed(2) * parseFloat(_unitCost).toFixed(2);
                                _$totalCost.val(_totalCost.toCommaSeparatedDecimal());
                                total();
                            }else{
                                _$totalCost.val("");
                            }
                    });
                    
                    _$grid.find("[name='seq_no']").attr("readonly",true);
                    _$grid.find("[name='total_cost']").attr("disabled",true);
                    _$grid.find("[name='unit_cost']").maskMoney();
                    _$grid.find("[name='unit_cost']").addClass("numeric");
                    zsi.initInputTypesAndFormats();
                    setFooterFreezed(_$grid);
                    
                }
            });
        });
    }
    
    function setFooterFreezed(zGridId){ 
        var _zRows = $(zGridId).find(".zGridPanel.right .zRows");
        var _tableRight   = _zRows.find("#table");
        var _zRowsHeight =   _zRows.height() - 38;
        var _everyZrowsHeight = $(".zRow:not(:contains('Overall Cost'))");
        var _arr = [];
        var _height = 0;
        var _zTotal = _tableRight.find(".zTotal");
        
        _everyZrowsHeight.each(function(){
            if(this.clientHeight) _arr.push(this.clientHeight);
        });
        
        for (var i = 0; i < _arr.length; i++){
           _height += _arr[i];
        }
        
        _zTotal.css({"top": _zRowsHeight}); 
        
        if(_zRows.find(".zRow").length == 1){
            _zTotal.addClass("hide");
        }else{
            if(_tableRight.height() > _zRowsHeight){
                _tableRight.parent().scroll(function() {
                   _zTotal.css({"top":_zRowsHeight - ( _tableRight.offset().top - _zRows.offset().top) });
                });
            }else{
                _zTotal.css({"top": _height});
            }
        }
    } 
    
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
    
   /* function submitReplacementParts(){
        var _$frm = $("#formPMS")
            ,_$grid = $("#gridReplacementParts");
        
        _$frm.find("[name='pms_id']").val(gPMSid);
        _$grid.find("[name='part_qty'], [name='unit_cost']").each(function(){
            this.value = this.value.replace(/,/g, "");
        });
        _$grid.jsonSubmit({
             procedure: "part_replacements_upd" 
             ,notIncludes: ["total_cost"]
             ,onComplete: function(data){
                if(data.isSuccess){
                    zsi.form.showAlert("alert"); 
                    
                    $("#btnNew").removeClass("hide");
                    dispalyReplacementParts();
                }
             }
        });
    }*/
    
    function markPMMandatory(){
        $("#divVehiclePMS").markMandatory({       
            "groupNames":[
                {
                     "names" : ["pms_date","vehicle_id","odo_reading"]
                } 
            ]      
            ,"groupTitles":[ 
                 {"titles" : ["PM Date","Vehicle","ODO Reading"]}
            ]
        }); 
    }
    
    function markReplacementPartsMandatory(){
        $("#gridReplacementParts").markMandatory({       
            "groupNames":[
                {
                     "names" : ["part_id","part_qty","unit_id","unit_cost"]
                    ,"type":"M"
                } 
            ]      
            ,"groupTitles":[ 
                 {"titles" : ["Part","Part Qty.","Unit","PM Cost"]}
            ]
        }); 
    }
    
    $("#btnNew").click(function () {
        var _params = ['#p','vehicle-pms'].join("/");
        window.open(_params,"_self");    
    }); 
    $("#service_amount").on('keyup', function(){
        var _$thisVal = $(this).val().replace(/,/g, "");
        
        $("#total_pms_amount").val(parseFloat((_$thisVal) + gTotal).toCommaSeparatedDecimal());
    });
    
    $("#btnSave").click(function () {
        var _$frm = $("#formPMS");
        var _frm = _$frm[0];
        var _formData = new FormData(_frm);  
        if( ! _frm.checkValidity() ){
            _$frm.addClass('was-validated');
        }else{   
            _$frm.removeClass('was-validated');
            $('#myModal').modal('show');
        }
    });
    
    $("#btnSavePMS").click(function () {
        var _$frm = $("#formPMS");
        var _$serviceAmt = _$frm.find("#service_amount,#pm_amount,total_pms_amount");
            _$serviceAmt.each(function(){
                this.value = this.value.replace(/,/g, "");
            });
        _$frm.jsonSubmit({
             procedure: "vehicle_pms_upd"
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   gPMSid = data.returnValue;
                   _$frm.find("#pms_id").val(gPMSid)
                   
                  if(data.isSuccess===true) zsi.form.showAlert("alert");
                  $("form").removeClass('was-validated');
                  $("#myModal").find("#msg").text("Data successfully saved.");
                  $("#myModal").find("#msg").css("color","green");
                  setTimeout(function(){
                      $("#myModal").modal('toggle');
                      $("#pms_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
                      modalTxt();
                  },1000); 
                    var _$grid = $("#gridReplacementParts");
                    _$grid.find(".zRow").find("[name='pms_id']").val(gPMSid);
                    _$grid.find("[name='part_qty'], [name='unit_cost']").each(function(){
                        this.value = this.value.replace(/,/g, "");
                    });
                    _$grid.jsonSubmit({
                         procedure: "part_replacements_upd" 
                         ,notIncludes: ["total_cost"]
                         ,onComplete: function(data){
                            if(data.isSuccess){
                                zsi.form.showAlert("alert"); 
                                
                                $("#btnNew").removeClass("hide");
                                dispalyReplacementParts();
                            }
                         }
                    });
                }else{
                  $("#myModal").find("#msg").text("Something went wrong when saving the data.");
                  $("#myModal").find("#msg").css("color","red");
                }
            }
        }); 
    });
    
    return _pub;
})();                                