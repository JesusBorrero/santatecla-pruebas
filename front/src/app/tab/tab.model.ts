import {Module} from '../itinerary/module/module.model';

export class Tab {

  link: string;
  isActive: boolean;
  closeLink: string;

  isAdmin = true;

  studentLessonSlideNumber: number;
  studentLessonSlideProgress: number;

  openedModuleNodes: number[];

  constructor(private _type: string, private _id: number, public _name: string, public _unitId: string, private _courseId: number, private _moduleId: number) {
    this.updateLink(_type, _id, _name, _unitId, _courseId, _moduleId);
    this.studentLessonSlideNumber = 0;
    this.studentLessonSlideProgress = 0;
    this.openedModuleNodes = [];
  }

  updateLink(type: string, id: number, name: string, unitId: string, courseId: number, moduleId: number) {
    if (type === 'Unidad') {
      if (this.id === 0) {
        this.link = '/unit';
      } else {
        this.link = '/unit/' + id;
      }
      this.closeLink = '/unit';
    } else if (type === 'Curso') {
      if (this.id === 0) {
        this.link = '/courses';
      } else {
        this.link = '/course/' + id;
      }
      this.closeLink = '/courses';
    } else if (type === 'Itinerario') {
      if (unitId !== null) {
        this.link = '/units/' + unitId + '/modules/' + id;
      } else {
        this.link = '/course/' + courseId + '/modules/' + id;
      }
      if (courseId !== null) {
        this.closeLink = '/courses';
      } else {
        this.closeLink = '/unit';
      }
    } else if (type === 'Lección') {
      if (this.isAdmin) {
        if (moduleId !== null) {
          this.link = '/units/' + unitId + '/modules/' + moduleId + '/lessons/' + id;
        } else {
          this.link = '/units/' + unitId + '/lessons/' + id;
        }
        this.closeLink = '/unit';
      } else {
        this.link = '/course/' + courseId + '/units/' + unitId + '/modules/' + moduleId + '/lessons/' + id;
        this.closeLink = '/course/' + this.courseId + '/modules/' + moduleId;
      }

    } else if (type === 'DefinitionQuestion') {
        this.link = '/unit/' + this.unitId + '/question/DefinitionQuestion/' + this.id;
        this.closeLink = '/unit';
    } else if (type === 'TestQuestion') {
      this.link = '/unit/' + this.unitId + '/question/TestQuestion/' + this.id;
      this.closeLink = '/unit';
    } else if (type === 'ListQuestion') {
      this.link = '/unit/' + this.unitId + '/question/ListQuestion/' + this.id;
      this.closeLink = '/unit';
    }

    this.isActive = true;
  }

  get type(): string {
    return this._type;
  }

  get id(): number {
    return this._id;
  }

  get name(): string {
    return this._name;
  }

  get courseId(): number {
    return this._courseId;
  }

  get unitId(): string {
    return this._unitId;
  }

  get moduleId(): number {
    return this._moduleId;
  }

  setIsNotAdmin() {
    this.isAdmin = false;
    this.updateLink(this.type, this.id, this.name, this.unitId, this.courseId, this.moduleId);
  }

  addOpenedNode(id: number, module: Module) {
    if (this.openedModuleNodes.indexOf(id) !== -1) {
      this.openedModuleNodes.splice(this.openedModuleNodes.indexOf(id), 1);
      this.removeChildModules(module);
      return false;
    } else {
      this.openedModuleNodes.push(id);
      return true;
    }
  }

  studentAddOpenedNode(module: Module, moduleId: number) {
    if (this.openedModuleNodes.indexOf(module.id) === -1) {
      this.openedModuleNodes.push(module.id);
    }
    this.studentAddOpenedNodeRecursive(module, moduleId, module.id);
  }

  studentAddOpenedLesson(module: Module, lessonId: number) {
    if (module.id === lessonId) {
      return true;
    } else if (module.blocks) {
      for (let block of module.blocks) {
        if (this.studentAddOpenedLesson(block, lessonId)) {
          if (this.openedModuleNodes.indexOf(block.id) === -1 && block.id !== lessonId) {
            this.openedModuleNodes.push(block.id);
          }
          return true;
        }
      }
      return false;
    } else {
      return false;
    }
  }

  studentAddOpenedNodeRecursive(module: Module, moduleId: number, rootId: number) {
    if (module.blocks) {
      if (module.id === moduleId) {
        return true;
      } else if (module.blocks) {
        for (let block of module.blocks) {
          if (this.studentAddOpenedNodeRecursive(block, moduleId, rootId)) {
            if (this.openedModuleNodes.indexOf(block.id) === -1 && block.id - rootId !== 0) {
              this.openedModuleNodes.push(block.id);
            }
            return true;
          }
        }
        return false;
      }
    } else {
      return false;
    }
  }

  removeChildModules(module: Module) {
    if (module.blocks) {
      for (let block of module.blocks) {
        if (this.openedModuleNodes.indexOf(block.id) !== -1) {
          this.openedModuleNodes.splice(this.openedModuleNodes.indexOf(block.id), 1);
        }
        this.removeChildModules(block);
      }
    }
  }

}
