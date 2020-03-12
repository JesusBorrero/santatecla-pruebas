export class Tab {

  constructor(private _type: string, private _id: number, private _name: string, private _course: number) {}

  get type(): string {
    return this._type;
  }

  get id(): number {
    return this._id;
  }

  get name(): string {
    return this._name;
  }

  get course(): number {
    return this._course;
  }

  get active(): boolean {
    return (window.location.href.includes(this.type + '/' + String(this.id)));
  }

}
