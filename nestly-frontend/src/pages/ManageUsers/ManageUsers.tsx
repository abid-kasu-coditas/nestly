import { useState } from 'react';
import { PrimaryBtn } from '../../components/ui-components/Button/Button';
import styles from './ManageUsers.module.scss'
import AddUserModal from '../../components/ui-components/AddUserModal/AddUserModal';

const ManageUsers = () => {
  const [isFormOpen, setIsFormOpen] = useState(true);
  return (
    <div className={styles.ManageUsers}>
          {isFormOpen ? <AddUserModal/> : ""}
          <div className={styles.ManageUsersHeader}>
            <h2>Manage Users</h2>
            <PrimaryBtn>+ ADD User</PrimaryBtn>
          </div>
          <div className={styles.UsersList}>
            
          </div>
    </div>
  )
}

export default ManageUsers;