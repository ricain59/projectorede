HTTP/1.1 200 OK
Server: nginx
Date: Thu, 15 Dec 2011 00:43:04 GMT
Content-Type: application/x-javascript
Content-Length: 13269
Last-Modified: Fri, 10 Nov 2006 09:52:58 GMT
Connection: close
Accept-Ranges: bytes

var n_cache        = new Array();

var c_cache        = new Array();

var comm_id;

var comm_edit_id;

var s_id;

var e_id;



function MenuNewsBuild( m_id, event ){



var menu=new Array()



menu[0]='<a onclick="ajax_prep_for_edit(\'' + m_id + '\', \'' + event + '\'); return false;" href="#">' + menu_short + '</a>';

menu[1]='<a href="' + dle_root + 'admin.php?mod=editnews&action=editnews&id=' + m_id + '" target="_blank">' + menu_full + '</a>';



return menu;

}



function ajax_cancel_for_edit( news_id )

{

        if ( n_cache[ news_id ] != "" )

        {

                document.getElementById( 'news-id-'+news_id ).innerHTML = n_cache[ news_id ];

        }



        return false;

}



function whenCompletedSave(){

n_cache[ e_id ] = '';

}



function ajax_save_for_edit( news_id, event )

{

        var ajax = new dle_ajax();

        var allow_br = 0;



        if (document.getElementById('allow_br_'+news_id).checked) { allow_br = 1; }



        e_id = news_id;

        ajax.onShow ('');

        var news_txt = ajax.encodeVAR( document.getElementById('edit-news-'+news_id).value );

        var news_title = ajax.encodeVAR( document.getElementById('edit-title-'+news_id).value );

        var varsString = "news_txt=" + news_txt;

        ajax.setVar("id", news_id);

        ajax.setVar("allow_br", allow_br);

        ajax.setVar("news_title", news_title);

        ajax.setVar("field", event);

        ajax.setVar("action", "save");

        ajax.requestFile = dle_root + "engine/ajax/editnews.php";

        ajax.method = 'POST';

        ajax.element = 'news-id-'+news_id;

        ajax.onCompletion = whenCompletedSave;

        ajax.sendAJAX(varsString);



        return false;

}



function whenCompleted(){



        var post_main_obj = document.getElementById( 'news-id-' + s_id );

        var post_box_top  = _get_obj_toppos( post_main_obj );



                        if ( post_box_top )

                        {

                                scroll( 0, post_box_top - 70 );

                        }



}



function ajax_prep_for_edit( news_id, event )

{

        if ( ! n_cache[ news_id ] || n_cache[ news_id ] == '' )

        {

                n_cache[ news_id ] = document.getElementById( 'news-id-'+news_id ).innerHTML;

        }



        var ajax = new dle_ajax();

        s_id = news_id;

        ajax.onShow ('');

        var varsString = "";

        ajax.setVar("id", news_id);

        ajax.setVar("field", event);

        ajax.setVar("action", "edit");

        ajax.requestFile = dle_root + "engine/ajax/editnews.php";

        ajax.method = 'GET';

        ajax.element = 'news-id-'+news_id;

        ajax.onCompletion = whenCompleted;

        ajax.sendAJAX(varsString);



        return false;

}





function whenCompletedCommentsEdit(){



        var post_main_obj = document.getElementById( 'comm-id-' + comm_id );

        var post_box_top  = _get_obj_toppos( post_main_obj );



                        if ( post_box_top )

                        {

                                scroll( 0, post_box_top - 70 );

                        }



}



function MenuCommBuild( m_id ){



var menu=new Array()



menu[0]='<a onclick="ajax_comm_edit(\'' + m_id + '\'); return false;" href="#">' + menu_short + '</a>';

menu[1]='<a href="' + dle_root + '?do=comments&action=comm_edit&id=' + m_id + '">' + menu_full + '</a>';



return menu;

}



function FotoMenuCommBuild ( m_id, user ) {

 var menu=new Array()



 menu[0]='<a onclick="ajax_foto_comm_edit(\'' + m_id + '\'); return false;" href="#">' + menu_short + '</a>';

 menu[1]='<a href="' + dle_root + '?do=album&action=comm_edit&id=' + m_id + '&user='+user+'">' + menu_full + '</a>';



return menu;

}



// -----------------------------------------------------------------------------

//   Фотоальбом!

// -----------------------------------------------------------------------------



function ajax_foto_comm_edit ( c_id )

{

        if ( ! c_cache[ c_id ] || c_cache[ c_id ] == '' )

        {

                c_cache[ c_id ] = document.getElementById( 'comm-id-'+c_id ).innerHTML;

        }



        var ajax = new dle_ajax();

        comm_id = c_id;

        ajax.onShow ('');

        var varsString = "";

        ajax.setVar("id", c_id);

        ajax.setVar("action", "edit");

        ajax.requestFile = dle_root + "engine/ajax/foto_editcomments.php";

        ajax.method = 'GET';

        ajax.element = 'comm-id-'+ c_id;

        ajax.onCompletion = whenCompletedCommentsEdit;

        ajax.sendAJAX(varsString);

        return false;

}



function ajax_foto_cancel_comm_edit( c_id )

{

        if ( n_cache[ c_id ] != "" )

        {

                document.getElementById( 'comm-id-'+c_id ).innerHTML = c_cache[ c_id ];

        }



        return false;

}



function ajax_foto_save_comm_edit( c_id )

{

        var ajax = new dle_ajax();

        comm_edit_id = c_id;

        ajax.onShow ('');

        var comm_txt = ajax.encodeVAR( document.getElementById('edit-comm-'+c_id).value );

        var varsString = "comm_txt=" + comm_txt;

        ajax.setVar("id", c_id);

        ajax.setVar("action", "save");

        ajax.requestFile = dle_root + "engine/ajax/foto_editcomments.php";

        ajax.method = 'POST';

        ajax.element = 'comm-id-'+c_id;

        ajax.onCompletion = whenCompletedSaveComments;

        ajax.sendAJAX(varsString);



        return false;

}



// -----------------------------------------------------------------------------



function ajax_comm_edit( c_id )

{

        if ( ! c_cache[ c_id ] || c_cache[ c_id ] == '' )

        {

                c_cache[ c_id ] = document.getElementById( 'comm-id-'+c_id ).innerHTML;

        }



        var ajax = new dle_ajax();

        comm_id = c_id;

        ajax.onShow ('');

        var varsString = "";

        ajax.setVar("id", c_id);

        ajax.setVar("action", "edit");

        ajax.requestFile = dle_root + "engine/ajax/editcomments.php";

        ajax.method = 'GET';

        ajax.element = 'comm-id-'+c_id;

        ajax.onCompletion = whenCompletedCommentsEdit;

        ajax.sendAJAX(varsString);

        return false;

}



function ajax_cancel_comm_edit( c_id )

{

        if ( n_cache[ c_id ] != "" )

        {

                document.getElementById( 'comm-id-'+c_id ).innerHTML = c_cache[ c_id ];

        }



        return false;

}



function whenCompletedSaveComments(){

 c_cache[ comm_edit_id ] = '';

}



function ajax_save_comm_edit( c_id )

{

        var ajax = new dle_ajax();

        comm_edit_id = c_id;

        ajax.onShow ('');

        var comm_txt = ajax.encodeVAR( document.getElementById('edit-comm-'+c_id).value );

        var varsString = "comm_txt=" + comm_txt;

        ajax.setVar("id", c_id);

        ajax.setVar("action", "save");

        ajax.requestFile = dle_root + "engine/ajax/editcomments.php";

        ajax.method = 'POST';

        ajax.element = 'comm-id-'+c_id;

        ajax.onCompletion = whenCompletedSaveComments;

        ajax.sendAJAX(varsString);



        return false;

}



function doFavorites( fav_id, event )

{

        var ajax = new dle_ajax();

        ajax.onShow ('');

        var varsString = "fav_id=" + fav_id;

        ajax.setVar("action", event);

        ajax.setVar("skin", dle_skin);

        ajax.requestFile = dle_root + "engine/ajax/favorites.php";

        ajax.method = 'GET';

        ajax.element = 'fav-id-'+fav_id;

        ajax.sendAJAX(varsString);



        return false;

}



function CheckLogin()

{

        var ajax = new dle_ajax();

        var name = ajax.encodeVAR( document.getElementById('name').value );

        ajax.onShow ('');

        var varsString = "name=" + name;

        ajax.requestFile = dle_root + "engine/ajax/registration.php";

        ajax.method = 'POST';

        ajax.element = 'result-registration';

        ajax.sendAJAX(varsString);



        return false;

}



function doCalendar(month, year){

        var ajax = new dle_ajax();

        ajax.onShow ('');

        var varsString = "";

        ajax.setVar("year", year);

        ajax.setVar("month", month);

        ajax.requestFile = dle_root + "engine/ajax/calendar.php";

        ajax.method = 'GET';

        ajax.element = 'calendar-layer';

        ajax.sendAJAX(varsString);

}



function ShowBild(sPicURL) {

 window.open(dle_root + 'engine/modules/imagepreview.php?image='+sPicURL, '', 'resizable=1,HEIGHT=200,WIDTH=200, top=0, left=0, scrollbars=yes');

}



function doRate(){

        var ajax = new dle_ajax();

        var form = document.getElementById('rating');

        ajax.onShow ('');

        var varsString = "go_rate=" + form.go_rate.value;

        ajax.setVar("news_id", form.news_id.value);

        ajax.setVar("skin", dle_skin);

        ajax.requestFile = dle_root + "engine/ajax/rating.php";

        ajax.method = 'GET';

        ajax.element = 'ratig-layer';

        ajax.sendAJAX(varsString);

}



function doFotoRate(){

        var ajax = new dle_ajax();

        var form = document.getElementById('rating');

        var varsString = "go_rate=" + form.go_rate.value;



        ajax.onShow ('');

        ajax.setVar("foto_id", form.foto_id.value);

        ajax.setVar("skin", dle_skin);

        ajax.requestFile = dle_root + "engine/ajax/foto_rating.php";

        ajax.method = 'GET';

        ajax.element = 'ratig-layer';

        ajax.sendAJAX(varsString);

}





var dle_comments_ajax = new dle_ajax();



function whenCommentsAdded(){

        dle_comments_ajax.onHide();

        document.getElementById( 'dle-ajax-comments' ).innerHTML += dle_comments_ajax.response;



        var post_box_top  = _get_obj_toppos( document.getElementById( 'dle-ajax-comments' ) );



                        if ( post_box_top )

                        {

                                scroll( 0, post_box_top - 70 );

                        }

        var form = document.getElementById('dle-comments-form');

        form.comments.value = '';

}



function doAddComments(){



        var form = document.getElementById('dle-comments-form');



        if (dle_wysiwyg == "yes") {

        document.getElementById('comments').value = oEdit1.getXHTMLBody();

        form.submit();

        }

        else {



        if (form.comments.value == '' || form.name.value == '')

        {

                alert ( dle_req_field );

                return false;

        }

        dle_comments_ajax.onShow ('');

        var varsString = "post_id=" + form.post_id.value;

        dle_comments_ajax.setVar("comments", dle_comments_ajax.encodeVAR(form.comments.value));

        dle_comments_ajax.setVar("name", dle_comments_ajax.encodeVAR(form.name.value));

        dle_comments_ajax.setVar("mail", dle_comments_ajax.encodeVAR(form.mail.value));

        dle_comments_ajax.setVar("skin", dle_skin);

        dle_comments_ajax.requestFile = dle_root + "engine/ajax/addcomments.php";

        dle_comments_ajax.method = 'POST';

        dle_comments_ajax.onCompletion = whenCommentsAdded;

        dle_comments_ajax.sendAJAX(varsString);
        



        }

}



function doAddFotoComments(){


        var form = document.getElementById('dle-comments-form');



        if (dle_wysiwyg == "yes") {

          document.getElementById('comments').value = oEdit1.getXHTMLBody();

          form.submit();

        } else {



        if (form.comments.value == '' || form.name.value == '')

        {

                alert ( dle_req_field );

                return false;

        }

        dle_comments_ajax.onShow ('');

        var varsString = "foto_id=" + form.foto_id.value;

        dle_comments_ajax.setVar("user", dle_comments_ajax.encodeVAR(form.user.value));

        dle_comments_ajax.setVar("comments", dle_comments_ajax.encodeVAR(form.comments.value));

        dle_comments_ajax.setVar("skin", dle_skin);

        dle_comments_ajax.requestFile = dle_root + "engine/ajax/foto_addcomments.php";

        dle_comments_ajax.method = 'POST';
        
        dle_comments_ajax.onCompletion = whenCommentsAdded;
        
        dle_comments_ajax.sendAJAX(varsString)

        }

}



function dle_copy_quote(qname)

{

 dle_txt=''



        if (document.getSelection)

        {

         dle_txt=document.getSelection()

        }

        else if (document.selection)

        {

         dle_txt=document.selection.createRange().text;

        }

        if (dle_txt.replace(" ","") != "")

        {

         dle_txt='[QUOTE='+qname+']'+dle_txt+'[/QUOTE]\n'

        }

}



function dle_ins(name)

{

var input=document.getElementById('dle-comments-form').comments;

var finalhtml = "";



        if (dle_wysiwyg == "no") {

                if (dle_txt!= "") {

                        input.value += dle_txt

                }

                else {

                        input.value += "[b]"+name+"[/b],"+"\n";

                }

        } else {

                if (dle_txt!= "") {

                        finalhtml = dle_txt;

                }

                else {

                        finalhtml = "<b>"+name+"</b>,"+"<br />";

                }

        oUtil.obj.insertHTML(finalhtml);

        }



}


