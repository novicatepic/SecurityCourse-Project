import { Room } from "./Room";
import { User } from "./User";


export class UserPermissionsRoom {
    
    public user?: User;
    public room?: Room;

    constructor(public userId:number,public roomId: number, 
        public canCreate: boolean,public canUpdate: boolean,public canDelete:boolean) {
        
    }


}