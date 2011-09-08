%{
    ( _arg ) &&  ( _var = _arg);
    
    if(! _var) {
        throw new play.exceptions.TagInternalException("var attribute cannot be empty for cms.rootNavigationItem tag");
    }
    
    _var = plugins.cms.Tag.rootNavigationItem();
}%