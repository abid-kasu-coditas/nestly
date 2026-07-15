import { Controller, useForm } from 'react-hook-form'
import styles from './ProfilePage.module.scss'
import { PrimaryBtn } from '../../components/ui-components/Button/Button'
import { useUpdateProfileMutation } from '../../redux/slices/profileApiSlice';

const ProfilePage = () => {

    const { control, handleSubmit } = useForm();
    const [ uploadPhoto ] = useUpdateProfileMutation();
    const handleProfileUpload = async(data: any) => {
        console.log("Uploading photo")
        const response = await uploadPhoto(data);
        console.log(response)
    }


  return (
    <div  className={styles.ProfilePage}>
        <form className={styles.ProfilePageForm} onSubmit={handleSubmit(handleProfileUpload)}>
            <Controller
                name='filename'
                control={control}
                render={({field})=>{
                    return <input type='file' {...field}></input>
                }}
            />
            <PrimaryBtn>Upload</PrimaryBtn>
        </form>

    </div>
  )
}

export default ProfilePage