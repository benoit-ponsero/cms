%{
    ( _arg ) &&  ( _code = _arg);
    
    if(! _code) {
        throw new play.exceptions.TagInternalException("code attribute cannot be empty for cms.translate tag");
    }

}%
${ plugins.cms.Tag.translate(_code) }