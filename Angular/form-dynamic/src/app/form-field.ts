/*
Define different "types" of fields such as textbox, radio, checkbox, and validator (i.e. for email)
The "options" array is used as parameter for the constructor. 
It could be used as an extension to undefined fields (i.e. config params) = rename as "params"?
*/
export class FormField<T> {
  value: T;
  key: string;
  label: string;
  required: boolean;
  validator: string;
  order: number;
  controlType: string;
  type: string;
  options: { key: string; value: string }[];

  constructor(
    p: {
      value?: T;
      key?: string;
      label?: string;
      required?: boolean;
      validator?: string;
      order?: number;
      controlType?: string;
      type?: string;
      options?: { key: string; value: string }[];
    } = {}
  ) {
    //this.setValue(options.value);
    this.value = p.value;
    this.key = p.key || "";
    this.label = p.label || "";
    this.required = !!p.required;
    this.validator = p.validator || "";
    this.order = p.order === undefined ? 1 : p.order;
    this.controlType = p.controlType || "";
    this.type = p.type || "";
    this.options = p.options || [];
  }

  public setValue(val: T) {
    this.value = val;
  }

  public getValue() {
    return this.value;
  }

}