import { BaseClass } from "./base-class";
import { Ec2dltHttpService } from "./ec2dlt-http-service";

export abstract class BaseService<T extends BaseClass> {

  prefix = '';

  constructor(public http: Ec2dltHttpService) { }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Récupére la liste de tous les enregistrements
   * @returns Promise
   */
  findAll() {
    return this.http.get(this.prefix);
  }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Récupére un enregistrement à partir de son id
   * @param id
   * @returns Promise
   */
  findOneById(id: number) {
    return this.http.get(this.prefix + '/' + id);
  }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Récupére un enregistrement à partir de son uuid
   * @param id
   * @returns Promise
   */
  findOneByUuid(uuid: string) {
    return this.http.get(this.prefix + '/' + uuid);
  }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Enregistt
   * @returns Promise
   * @param object
   */
  save(object: T) {
    return this.http.post(this.prefix, object);
  }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Met à jour un enregistrement donnée
   * @param object
   * @returns Promise
   */
  update(object: T) {
    return this.http.put(`${this.prefix}/${object.id}`, object);
  }
  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Supprime un objet donné
   * @param object
   * @returns Promise
   */
  removeObject(object: T) {
    return this.http.delete(`${this.prefix}/${object.id}`);
  }

  /**
   * @author Moussa Anne
   * @since 23.01.22
   * @description Supprime un objet à partir de son id
   * @param id
   * @returns Promise
   */
  removeById(id: number) {
    return this.http.delete(`${this.prefix}/${id}`);
  }
}
