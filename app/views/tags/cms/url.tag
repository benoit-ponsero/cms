%{
    ( _arg ) &&  ( _action = _arg);
    
    if(! _action) {
        throw new play.exceptions.TagInternalException("action attribute cannot be empty for cms.url tag");
    }

}%
${ plugins.cms.Tag.url(_action) }