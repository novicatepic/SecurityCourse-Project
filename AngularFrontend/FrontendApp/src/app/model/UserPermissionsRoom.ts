

export class UserPermissionsRoom {
    

    constructor(public userId:number,public roomId: number, 
        public canCreate: boolean,public canUpdate: boolean,public canDelete:boolean) {
        
    }


}