import { Controller, useForm } from 'react-hook-form'
import styles from './AddUserModal.module.scss'
import type { AddUserData } from './AddUserModal.types';
import { PrimaryBtn } from '../Button/Button';

const AddUserModal = () => {

    const { handleSubmit, control, formState: { isLoading }} = useForm<AddUserData>();

    
  return (
    <div className={styles.AddUserModal}>
        <div className={styles.AddUserModalContainer}>
            <h2>Add User</h2>
            <form className={styles.AddUserModalForm}>
                <Controller 
                    name='email'
                    control={control}
                    render={({field})=>{
                        return <input placeholder='user@example.com' {...field}></input>
                    }}
                />
                <PrimaryBtn>Add</PrimaryBtn>
            </form>
        </div>
    </div>
  )
}

export default AddUserModal