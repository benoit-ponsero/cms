var cms = {
    
    editor: null
    , language: null
    , requestedResource: null
    , modified : false
    , toolbar_opened : false
    , opts : {
        cssClass : 'el-rte cmsrte',
        lang     : 'fr',
        toolbar  : 'complete',
        allowSource : true,
        cssfiles : ['/public/javascripts/elrte-1.3/css/elrte-inner.css']
        ,fmOpen : function(callback) {
            $('<div id="myelfinder" />').elfinder({
             url : '/--editor/browser',
             lang : 'fr',
             dialog : { width : 900, modal : true, title : 'Fichiers' }, // open in dialog window
             closeOnEditorCallback : true, // close after file select
             editorCallback : callback     // pass callback to file manager
            })
        }
    }
    , _rte : null // current elrte instance
    , _bodyPadTop : 0
    , init_toolbar : function (){
        var $body = $("body");
        $body.append('<div class="cms_opener">...</div>' +
                          '<div class="cms_toolbar">' +
                           '<div class="cms_actions">' +
                            '<span class="cms_left_actions">' +
                             '<span class="cms_action"><a href="javascript:cms.navigation();">Arborescence...</a></span>' +
                             '<span class="cms_action"><a href="javascript:cms.translation();">Traductions...</a></span>' +
                            '</span>' +
                            '<span class="cms_right_actions">' +
                             '<span class="cms_action cms_save"><a href="javascript:cms.save();">Sauvegarde</a></span>' +
                             '<span class="cms_action"><a href="javascript:cms.logout();">Déconnection</a></span>' +
                            '</span>' +
                           '</div>' +
                          '<div style="clear:both"></div>' +
                          '<div id="cms_editor_toolbar"></div>' +
                          '<div id="cms_navigation_location"></div>' +
                          '<form id="cms_logout" method="post"><input type="hidden" name="cms_logout" value="true"/></form>' +
                         '</div>');
                     
        var oldpadding = parseInt($body.css('padding-top').replace(/px/g,""), 10);
        
        $body.css('padding-top',  oldpadding + 40);
    }
    ,toolbar_toogle: function () {
        if (cms.toolbar_opened) {
            cms.toolbar_close();
        } else {
            cms.toolbar_open();
        }
    },

    toolbar_open: function () {
        
        $(".cms_toolbar").css('top', 0);
        cms.toolbar_opened = true;
    },

    toolbar_close: function () {
        $("#cms_navigation_location").hide();
        $(".cms_toolbar").animate({top:'-200px'}, 200, function () {
            cms.toolbar_opened = false;
        });
    }
    , save : function (){
            
        var data = {};

        data.lang     = cms.language;
        data.path     = cms.requestedResource;
        data.editors  = [];

        $("div.cms_editor").each(function() {

            var html = $(this).html();
            var code = $(this).data('code');

            var editor = {};

            editor.code = code;
            editor.content = html;

            data.editors.push (editor);
        });


        $.post('/--editor/save', data, function (){

            cms.modified = false;
        })
    }
    , kill_editor : function (){
        
        if (cms._rte != null){
            
            cms._rte.elrte('destroy');
            cms._rte.get(0).elrte = null;
            cms._rte = null;
            $("body").css("padding-top", cms._bodyPadTop);
            $("#cms_editor_toolbar").height(0);
        }
    }
    , logout : function (){
        
        $("#cms_logout").submit();
    }
};


(function($) {  
    
    elRTE.prototype.ui.prototype.buttons.source = function(rte, name) {
        this.constructor.prototype.constructor.call(this, rte, name);
        this.active = true;
	
	this.command = function() {
            var $tab = $(".cmsrte .tabsbar div.tab.active");
            if ($tab.hasClass("source")){
                $(".cmsrte .tab.editor").trigger('click');
                this.domElem.removeClass("active");
            }
            else {
                $(".cmsrte .tab.source").trigger('click');
                this.domElem.addClass("active");
            }
	}
	
	this.update = function() {
            this.domElem.removeClass('disabled');
        }

    }
    
    elRTE.prototype.options.panels.source = ['source'];
    elRTE.prototype.options.toolbars.tiny.splice(0,0,'source');
    elRTE.prototype.options.toolbars.compact.splice(0,0,'source');
    elRTE.prototype.options.toolbars.normal.splice(0,0,'source');
    elRTE.prototype.options.toolbars.complete.splice(0,0,'source');
    elRTE.prototype.options.toolbars.maxi.splice(0,0,'source');
    elRTE.prototype.options.toolbars.eldorado.splice(0,0,'source');
    
})(jQuery);

$(function (){
    
    depend.jqueryui(function (){
        
        $("#cms_user img").click(function () {
            $("#cms_user").toggleClass("opened");
        });
        
        if (!cms.logged){
            return;
        }
        
        cms.init_toolbar();
        
        $(".cms_opener").click (function(ev) {
            cms.toolbar_toogle();
        })
        
        $(document).click(function (e){
				
            var $target = $(e.target);

            if ($target.parents('.el-rte').length == 0 
                && $target.parents(".ui-dialog").length == 0
                && !$target.hasClass("ui-widget-overlay")
                && !$target.hasClass("ui-button-text")){
                
                cms.kill_editor();
            }
            return true;
        })

        $(".cms_editor").click(function (){

            cms.kill_editor();

            var h = $(this).height();
            cms.opts.height = (h>50) ? h : 50; 
            cms._rte = $(this).elrte(cms.opts);
            
            cms.toolbar_open();

            var top = $("#cms_editor_toolbar").offset().top - $(document).scrollTop();
                
            var toolbar_height  = $(".cmsrte .toolbar").height();
            
            $("#cms_editor_toolbar").height(toolbar_height+7);
            $(".cmsrte .toolbar").css('top', top);

            var $body = $("body");
            cms._bodyPadTop = parseInt($body.css("padding-top") . replace(/px/, ""), 10);

            $body.css("padding-top", cms._bodyPadTop + toolbar_height);

            return false;
        }).mouseenter(function (){
            $(this).addClass("hover");
        }).mouseleave(function (){
            $(this).removeClass("hover");
        });
        
        
        
    });
})

