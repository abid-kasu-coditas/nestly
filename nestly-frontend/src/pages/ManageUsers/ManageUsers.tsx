import { useState } from 'react';
import { PrimaryBtn, SecondaryBtn } from '../../components/ui-components/Button/Button';
import styles from './ManageUsers.module.scss'
import AddUserModal from '../../components/ui-components/AddUserModal/AddUserModal';
import Table from '../../components/ui-components/Table/Table';

const ManageUsers = () => {
  const [isFormOpen, setIsFormOpen] = useState(false);
  return (
    <div className={styles.ManageUsers}>
          {isFormOpen ? <AddUserModal closeModalFn={()=>{
            setIsFormOpen(false)
          }}/> : ""}
          <div className={styles.ManageUsersHeader}>
            <h2>Manage Users</h2>
            <PrimaryBtn onClick={()=> {
              setIsFormOpen(true)
            }}>+ ADD User</PrimaryBtn>
          </div>
          <div className={styles.UsersList}>
            <Table>
              <Table.TableHead>
                <Table.Th>Sr. No.</Table.Th>
                <Table.Th>Name</Table.Th>
                <Table.Th>Email</Table.Th>
                <Table.Th>Role</Table.Th>
                <Table.Th>Actions</Table.Th>
              </Table.TableHead>
              <Table.TableBody>
                <Table.TableRow>
                  <Table.TData>1</Table.TData>
                  <Table.TData>Abid Kasu</Table.TData>
                  <Table.TData>abidkasu@gmail.com</Table.TData>
                  <Table.TData>Owner</Table.TData>
                  <Table.TData>
                    <SecondaryBtn className={styles.DeleteBtn}>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn className={styles.DeleteBtn}>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn className={styles.DeleteBtn}>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
                <Table.TableRow>
                  <Table.TData>2</Table.TData>
                  <Table.TData>Nihar Kilje</Table.TData>
                  <Table.TData>kiljenihar@gmail.com</Table.TData>
                  <Table.TData>Tenant</Table.TData>
                  <Table.TData>
                    <SecondaryBtn>Delete</SecondaryBtn>
                    </Table.TData>
                </Table.TableRow>
              </Table.TableBody>
            </Table>
          </div>
    </div>
  )
}

export default ManageUsers;