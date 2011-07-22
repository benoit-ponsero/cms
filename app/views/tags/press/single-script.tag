%{
    ( _arg ) &&  ( _src = _arg);
    
    if(! _src) {
        throw new play.exceptions.TagInternalException("src attribute cannot be empty for press.single-script tag");
    }
}%
${ plugins.press.Plugin.addSingleJS(_src) }