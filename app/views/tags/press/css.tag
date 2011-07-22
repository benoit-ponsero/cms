%{
    ( _arg ) &&  ( _src = _arg);
    
    // compress defaults to true
    if(_compress == null) {
      _compress = true;
    }
    
    if(! _src) {
        throw new play.exceptions.TagInternalException("src attribute cannot be empty for stylesheet tag");
    }

}%
${ plugins.press.Plugin.addCSS(_src, _compress) }