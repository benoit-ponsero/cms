%{
    ( _arg ) &&  ( _src = _arg);
    
    // compress defaults to true
    if(_compress == null) {
      _compress = true;
    }
    
    if(! _src) {
        throw new play.exceptions.TagInternalException("src attribute cannot be empty for press.script tag");
    }

}%
${ plugins.press.Plugin.addJS(_src, _compress) }