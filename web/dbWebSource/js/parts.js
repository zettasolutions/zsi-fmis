  (function(){
        
    var  bs         = zsi.bs.ctrl
        ,svn        = zsi.setValIfNull
        ,gPartTypeId   = null
        ,gActiveTab = ""
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Parts");
        displayPartTypes()
        displayParts(); 
        gActiveTab = "part-types";
        
        $("#partTypeId").select2({placeholder: "PART TYPES",allowClear: true});
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e){
            var target = $(e.target).attr("href"); 
            
            console.log("target",target);
            
            if(target === "#nav-part-types"){
                gActiveTab = "part-types";
                $("#searchVal").val("");
                $("#partTypeDiv").addClass("hide");
                $("#dummyDiv").removeClass("hide");
                displayParts(gPartTypeId);
            }else{
                gActiveTab = "parts";
                $("#searchVal").val("");
                $("#partTypeDiv").removeClass("hide");
                $("#dummyDiv").addClass("hide");
                $("#assetId").val(gPartTypeId).trigger('change');
            }
        }); 
        
        console.log("client",app.userInfo);
    }; 
    
    function displaySelects(){
        $("#partTypeId").dataBind({
             sqlCode    : "D253" //dd_part_types_sel
            ,text       : "part_type"
            ,value      : "part_type_id" 
            ,required   : true
            ,onChange   : function(){ 
                gPartTypeId = this.val();  
            }
        });
    }
    
    function displayPartTypes(searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridPartTypes").dataBind({
             sqlCode        : "P251" //part_types_sel
            ,parameters     : {search_val:(searchVal ? searchVal : "")}
            ,height         : $(window).height() - 340 
            ,blankRowsLimit : 5
            ,dataRows   : [
                     {text: cb                                                                          ,width:25           ,style:"text-align:left"
                         ,onRender : function(d){
                             return app.bs({name:"part_type_id"             ,type:"hidden"              ,value: app.svn(d,"part_type_id")}) 
                                  + app.bs({name:"is_edited"                    ,type:"hidden"              ,value: app.svn(d,"is_edited")})
                                  + (d !== null ? app.bs({name:"cb"             ,type:"checkbox"}) : "" );
                         }
                     }
                    ,{text:"Part Type Code"        ,type:"input"       ,name:"part_type_code"         ,width:100          ,style:"text-align:left"}
                    ,{text:"Part Type"             ,type:"input"       ,name:"part_type"              ,width:400          ,style:"text-align:left"}
                    
                  ]
                  ,onComplete : function(o){
                    var _dRows = o.data.rows;
                    var _this  = this;
        	        var _zRow  = _this.find(".zRow");
        	        if(_dRows.length < 1) $(".nav-tabs").find(".nav-item").find("[aria-controls='nav-parts']").parent(".nav-item").hide();
        	        _zRow.unbind().click(function(){
        	            var _self=this;
        	            setTimeout(function(){ 
            	            var _i      = $(_self).index();
            	            var _data   = _dRows[_i];
            	            var _partTypeId  = _data.part_type_id;
            	            gPartTypeId = _partTypeId;
            	            displaySelects();
            	            $(".nav-tabs").find(".nav-item").find("[aria-controls='nav-parts']").parent(".nav-item").show();
            	            setTimeout(function(){
            	                $("#partTypeId").val(_partTypeId).trigger('change');
            	            }, 200);
                            displayParts(_partTypeId);

        	            }, 200);
        	        });
        	        _this.on('dragstart', function () {
                        return false;
                    });
                    _this.find("[name='cbFilter1']").setCheckEvent("#gridPartTypes input[name='cb']");
                  } 
            });
        } 
    
    function displayParts(part_type_id,searchVal){  
        var cb = app.bs({name:"cbFilter2",type:"checkbox"});
        $("#gridParts").dataBind({
             sqlCode            : "P252" //parts_sel
            ,parameters         : {part_type_id: part_type_id,search_val:(searchVal ? searchVal : "")}
	        ,height             : $(window).height() - 340 
            ,blankRowsLimit     : 5
            ,dataRows           : [
                    {text: cb                                                                          ,width:25           ,style:"text-align:left"
                        ,onRender : function(d){
                            return app.bs({name:"part_id"                           ,type:"hidden"              ,value: app.svn(d,"part_id")}) 
                                + app.bs({name:"is_edited"                          ,type:"hidden"              ,value: app.svn(d,"is_edited")})
                                + app.bs({name:"part_type_id"                       ,type:"hidden"              ,value: part_type_id})
                                + (d !== null ? app.bs({name:"cb"                   ,type:"checkbox"}) : "" );
                        }
                    }
                    ,{text:"Part Code"                       ,type:"input"          ,name:"part_code"           ,width:100       ,style:"text-align:left"}
                    ,{text:"Part Description"                ,type:"input"          ,name:"part_desc"           ,width:400       ,style:"text-align:left"}
                    
                ] 
                ,onComplete : function(d){
                    this.find("[name='cbFilter2']").setCheckEvent("#gridParts input[name='cb']");
                    
                } 
            });
        }
        
        
    $("#btnSavePartTypes").click(function(){
        $("#gridPartTypes").jsonSubmit({
            procedure:"part_types_upd"
            ,onComplete:function(data){
                if(data.isSuccess===true)zsi.form.showAlert("alert");
                displayPartTypes();
            }
        });
    });
    
    $("#btnDeletePartTypes").click(function(){
        zsi.form.deleteData({ 
                code:"ref-00019" 
               ,onComplete:function(data){
                     displayPartTypes();
               }
        });
    });
    
    $("#btnDeleteParts").click(function(){
        zsi.form.deleteData({ 
                code:"ref-00020" 
               ,onComplete:function(data){
                     displayPartTypes();
               }
        });
    });
            
    $("#btnSaveParts").click(function(){ 
        $("#gridParts").jsonSubmit({
             procedure: "parts_upd"
            //,optionalItems: ["is_active","status_id"] 
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayParts(gPartTypeId);
            } 
        }); 
    });
    
    $("#btnDeleteVehicles").click(function(){ 
        zsi.form.deleteData({ 
            code:"ref-00012"
           ,onComplete:function(data){
                displayInactiveVehicles(gPartTypeId);
                displayParts(gPartTypeId);
                $('#modalInactive').modal('toggle');
           }
        });
    });
    
    $("#btnFilterAsset").click(function(){ 
        displayParts(gPartTypeId);
    });
    
    $("#btnSearchVal").click(function(){ 
        var _searchVal = $.trim($("#searchVal").val()); 
        if(gActiveTab === "nav-vehicles") displayPartTypes(_searchVal);
        else displayParts(gPartTypeId,_searchVal);
        
    }); 
   $("#searchVal").on('keypress',function(e){
        var _searchVal = $.trim($("#searchVal").val()); 
        if(e.which == 13) {
           if(gActiveTab === "nav-vehicles") displayPartTypes(_searchVal);
           else displayParts(gPartTypeId,_searchVal);
        }
    });

    $("#searchVal").keyup(function(){
        if($(this).val() === "") {
            if(gActiveTab === "nav-vehicles") displayPartTypes();
            else displayParts(gPartTypeId);
        }
    });
    
    $("#btnResetVal").click(function(){
        $("#searchVal").val("");
        $("#nav-tab").find("[aria-controls='nav-vehicles']").hide();
    });
    
})();        