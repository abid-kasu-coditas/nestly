import { PrimaryBtn } from '../../components/ui-components/Button/Button';
import styles from './ManageUsers.module.scss'

const ManageUsers = () => {
  return (
    <div className={styles.ManageUsers}>
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